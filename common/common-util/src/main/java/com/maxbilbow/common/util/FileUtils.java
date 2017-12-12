package com.maxbilbow.common.util;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.lang.ref.WeakReference;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileUtils
{
  private static transient WeakReference<Logger> weakLogger;
  
  private static Logger logger()
  {
    final Logger logger;
    if (weakLogger == null)
      logger = null;
    else
      logger = weakLogger.get();
    
    if (logger == null)
      weakLogger = new WeakReference<>(LoggerFactory.getLogger(FileUtils.class));
    
    return logger;
  }
  
  @FunctionalInterface
  public interface HashingFunction {
    String hash(InputStream in) throws IOException;
  }
  
  public static String getChecksumForPath(Path dirToHash, boolean includeHiddenFiles, HashingFunction aHashingFunction) throws IOException
  {
    Vector<InputStream> fileStreams = new Vector<>();
    
    collectInputStreams(dirToHash, fileStreams, includeHiddenFiles);
    
    try (final SequenceInputStream seqStream = new SequenceInputStream(fileStreams.elements()))
    {
      return aHashingFunction.hash(seqStream);
    }
  }
  
  @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
  private static void collectInputStreams(Path f,
                                          List<InputStream> foundStreams,
                                          boolean includeHiddenFiles) throws IOException
  {
    final Logger logger = logger();
    if (!includeHiddenFiles && (f.getFileName().startsWith(".") || Files.isHidden(f)))
    {
      logger.debug("Skipped hidden file: " + f.getFileName());
    }
    else if (Files.isDirectory(f))
    {
      final Iterator<Path> it = Files.list(f)
              .sorted(Comparator.comparing(Path::getFileName))
              .iterator();
      while (it.hasNext())
        collectInputStreams(it.next(),foundStreams,includeHiddenFiles);
    }
    else
    {
      logger.debug("\tFound stream for: " + f.toAbsolutePath());
      foundStreams.add(Files.newInputStream(f));
    }
    
  }
  
  
  @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
  public static Path getMavenProjectPath(String aPath) throws IOException, URISyntaxException
  {
    final Path projectDir;
    {
      final URL here = ClassLoader.getSystemClassLoader().getResource("");
      assert here != null;
      Path project = Paths.get(here.toURI()).getParent();
      while (!Files.newDirectoryStream(project, file -> Objects.toString(file.getFileName()).equals("pom.xml"))
              .iterator()
              .hasNext())
      {
        project = project.getParent();
      }
      projectDir = project;
    }
    
    if (aPath == null || aPath.isEmpty())
      return projectDir;
    
    aPath = aPath.replaceAll("\\|/", File.separator);
    if (!aPath.startsWith(File.separator))
      aPath = File.separator + aPath;
    
    return Paths.get(projectDir + aPath);
  }
}
