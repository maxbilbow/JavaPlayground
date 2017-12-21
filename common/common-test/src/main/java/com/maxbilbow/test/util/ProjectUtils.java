package com.maxbilbow.test.util;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class ProjectUtils
{
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
        project = project.getParent();
      
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
