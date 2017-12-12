package com.maxbilbow.common.util;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.lang.ref.WeakReference;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class ChecksumUtil
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
      weakLogger = new WeakReference<>(LoggerFactory.getLogger(ChecksumUtil.class));
    
    return logger;
  }
  
  @FunctionalInterface
  public interface HashingFunction {
    byte[] hash(InputStream in) throws IOException;
  }
  
  public static byte[] md2(Path dirToHash, boolean includeHiddenFiles) throws IOException
  {
    return checksum(dirToHash,includeHiddenFiles, DigestUtils::md2);
  }
  
  public static String md2Hex(Path dirToHash, boolean includeHiddenFiles) throws IOException
  {
    return Hex.encodeHexString(md2(dirToHash,includeHiddenFiles));
  }
  
  public static byte[] md5(Path dirToHash, boolean includeHiddenFiles) throws IOException
  {
    return checksum(dirToHash,includeHiddenFiles, DigestUtils::md5);
  }
  
  public static String md5Hex(Path dirToHash, boolean includeHiddenFiles) throws IOException
  {
    return Hex.encodeHexString(md5(dirToHash,includeHiddenFiles));
  }
  
  public static byte[] sha1(Path dirToHash, boolean includeHiddenFiles) throws IOException
  {
    return checksum(dirToHash,includeHiddenFiles, DigestUtils::sha1);
  }
  
  public static String sha1Hex(Path dirToHash, boolean includeHiddenFiles) throws IOException
  {
    return Hex.encodeHexString(sha1(dirToHash,includeHiddenFiles));
  }
  
  public static byte[] sha256(Path dirToHash, boolean includeHiddenFiles) throws IOException
  {
    return checksum(dirToHash,includeHiddenFiles, DigestUtils::sha256);
  }
  
  public static String sha256Hex(Path dirToHash, boolean includeHiddenFiles) throws IOException
  {
    return Hex.encodeHexString(sha256(dirToHash,includeHiddenFiles));
  }
  
  public static byte[] sha384(Path dirToHash, boolean includeHiddenFiles) throws IOException
  {
    return checksum(dirToHash,includeHiddenFiles, DigestUtils::sha384);
  }
  
  public static String sha384Hex(Path dirToHash, boolean includeHiddenFiles) throws IOException
  {
    return Hex.encodeHexString(sha384(dirToHash,includeHiddenFiles));
  }
  
  public static byte[] sha512(Path dirToHash, boolean includeHiddenFiles) throws IOException
  {
    return checksum(dirToHash,includeHiddenFiles, DigestUtils::sha512);
  }
  
  public static String sha512Hex(Path dirToHash, boolean includeHiddenFiles) throws IOException
  {
    return Hex.encodeHexString(sha512(dirToHash,includeHiddenFiles));
  }
  
  private static byte[] checksum(Path dirToHash, boolean includeHiddenFiles, HashingFunction aHashingFunction) throws IOException
  {
    final Vector<InputStream> fileStreams = new Vector<>();
    
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
  
  
  
}
