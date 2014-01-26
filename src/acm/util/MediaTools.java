/*
 * @(#)MediaTools.java   1.0 06/08/25
 */

// ************************************************************************
// * Copyright (c) 2006 by the Association for Computing Machinery        *
// *                                                                      *
// * The Java Task Force seeks to impose few restrictions on the use of   *
// * these packages so that users have as much freedom as possible to     *
// * use this software in constructive ways and can make the benefits of  *
// * that work available to others.  In view of the legal complexities    *
// * of software development, however, it is essential for the ACM to     *
// * maintain its copyright to guard against attempts by others to        *
// * claim ownership rights.  The full text of the JTF Software License   *
// * is available at the following URL:                                   *
// *                                                                      *
// *          http://www.acm.org/jtf/jtf-software-license.pdf             *
// *                                                                      *
// ************************************************************************

package acm.util;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.StringTokenizer;

/* Class: MediaTools */

/**
 * This class implements a standard mechanism for loading images and sounds.
 */
public class MediaTools {

/* Constant: DEFAULT_IMAGE_PATH */
/**
 * The list of directories scanned for images, separated by colons.
 */
	public static final String DEFAULT_IMAGE_PATH = ".:images";

/* Constant: DEFAULT_AUDIO_PATH */
/**
 * The list of directories scanned for audio clips, separated by colons.
 */
	public static final String DEFAULT_AUDIO_PATH = ".:sounds";

/* Private constructor: MediaTools */
/**
 * Prevents anyone else from constructing this class.
 * @noshow
 */
	private MediaTools() {
		/* Empty */
	}

/* Static method: loadImage(name) */
/**
 * Searches the default image search path for an image with the specified name
 * and then loads it to create an <code>Image</code>.  The search process
 * consists of the following steps:
 *
 * <p><ol>
 * <li>Check to see if an image with that name has already been defined.  If
 *     so, return that image.<p>
 *
 * <li>Check to see if there is a resource available with that name whose
 *     contents can be read as an <code>Image</code>.  If so, read the image
 *     from the resource file.<p>
 *
 * <li>Load the image from a file with the specified name, relative to the
 *     application directory or the applet code base.
 * </ol><p>
 *
 * The second and third steps are repeated for each element of the image
 * search path, which consists of a list of directories separated by colons.
 *
 * <p>Unlike the <code>getImage</code> method in the <code>Applet</code> class,
 * <code>loadImage</code> waits for an image to be fully loaded before returning.
 *
 * @usage Image image = MediaTools.loadImage(name);
 * @param name The name of the image
 * @return A fully loaded <code>Image</code> object
 */
	public static Image loadImage(String name) {
		return loadImage(name, DEFAULT_IMAGE_PATH);
	}

/* Static method: loadImage(name, path) */
/**
 * Searches for an image with the given name and loads it to create an
 * <code>Image</code>.  Its operation is identical to the single-argument
 * <a href="#loadImage(String)"><code>loadImage</code></a> call except
 * in that this version allows the client to specify the search path
 * explicitly.
 *
 * @usage Image image = MediaTools.loadImage(name, path);
 * @param name The name of the image
 * @param path A string of directories names separated by colons
 * @return A fully loaded <code>Image</code> object
 * @noshow
 */
	public static Image loadImage(String name, String path) {
		Image image = (Image) imageTable.get(name);
		if (image != null) return image;
		if (name.endsWith(".gif")) {
			try {
				Class imageClass = Class.forName("images." + name.substring(0, name.length() - 4));
				Object compiledImage = imageClass.newInstance();
				Method getImage = imageClass.getMethod("getImage", new Class[0]);
				return (Image) getImage.invoke(compiledImage, new Object[0]);
			} catch (Exception ex) {
				/* Empty */
			}
		}
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		StringTokenizer tokenizer = new StringTokenizer(path, ":");
		while (image == null && tokenizer.hasMoreTokens()) {
			String prefix = tokenizer.nextToken();
			prefix = (prefix.equals(".")) ? "" : prefix + "/";
			URL url = null;
			boolean resourceFlag = false;
			try {
				url = RESOURCE_CLASS.getResource("/" + prefix + name);
				URLConnection connection = url.openConnection();
				if (connection == null || connection.getContentLength() <= 0) {
					url = null;
				} else {
					resourceFlag = true;
				}
			} catch (Exception ex) {
				/* Empty */
			}
			if (url == null) {
				Applet applet = JTFTools.getApplet();
				if (applet != null) {
					URL codeBase = applet.getCodeBase();
					if (codeBase != null) {
						try {
							url = new URL(codeBase, prefix + name);
						} catch (MalformedURLException ex) {
							/* Empty */
						}
					}
				}
			}
			if (url == null) {
				try {
					if (new File(prefix + name).canRead()) {
						image = toolkit.getImage(prefix + name);
					}
				} catch (SecurityException ex) {
					/* Empty */
				}
			} else {
				try {
					URLConnection connection = url.openConnection();
					if (resourceFlag || connection.getContentLength() > 0) {
						Object content = connection.getContent();
						if (content instanceof ImageProducer) {
							image = toolkit.createImage((ImageProducer) content);
						} else if (content != null) {
							image = toolkit.getImage(url);
						}
					}
				} catch (IOException ex) {
					/* Empty */
				}
			}
		}
		if (image == null) throw new ErrorException("Cannot find an image named " + name);
		loadImage(image);
		if (cachingEnabled) imageTable.put(name, image);
		return image;
	}

/* Static method: loadImage(image) */
/**
 * Makes sure that the image is fully loaded before returning.
 *
 * @usage image = MediaTools.loadImage(image);
 * @param image The <code>Image</code> which may not yet be loaded
 * @return The same <code>Image</code> after ensuring that it is fully loaded
 */
	public static Image loadImage(Image image) {
		MediaTracker tracker = new MediaTracker(JTFTools.createEmptyContainer());
		tracker.addImage(image, 0);
		try {
			tracker.waitForID(0);
		} catch (InterruptedException ex) {
		    throw new ErrorException("Image loading process interrupted");
		}
		return image;
	}

/* Static method: defineImage(name, image) */
/**
 * Inserts the given image into the image table under the specified name.
 *
 * @usage MediaTools.defineImage(name, image);
 * @param name The name for the image
 * @param image The image to be stored in the table
 */
	public static void defineImage(String name, Image image) {
		imageTable.put(name, image);
	}

/* Static method: flushImage(name) */
/**
 * Removes the image with the given name from the cache, allowing it
 * to be freed by the garbage collector.
 *
 * @usage MediaTools.flushImage(name);
 * @param name The name for the image
 */
	public static void flushImage(String name) {
		imageTable.remove(name);
	}

/* Static method: createImage(pixels, width, height) */
/**
 * Generates an image from an array of pixel values.
 *
 * @usage Image image = MediaTools.createImage(pixels, width, height);
 * @param pixels An array of <code>int</code> representing the pixels
 * @param width The width of the image
 * @param height The height of the image
 * @return An <code>Image</code> object
 */
	public static Image createImage(int[] pixels, int width, int height) {
		return Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(width, height, pixels, 0, width));
	}

/* Static method: createImage(in) */
/**
 * Generates an image from an input stream containing the data bytes for the
 * image formatted in image/gif format.
 *
 * @usage Image image = MediaTools.createImage(in);
 * @param in An input stream containing the data
 * @return An <code>Image</code> object
 */
	public static Image createImage(InputStream in) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			for (int ch = in.read(); ch != -1; ch = in.read()) {
				out.write(ch);
			}
			return Toolkit.getDefaultToolkit().createImage(out.toByteArray());
		} catch (Exception ex) {
			throw new ErrorException("Exception: " + ex);
		}
	}

/* Static method: createImage(hexData) */
/**
 * Generates an image from a string array that provides the pixel values.
 *
 * @usage Image image = MediaTools.createImage(hexData);
 * @param hexData A hex string array representing a .gif value
 * @return An <code>Image</code> object
 */
	public static Image createImage(String[] hexData) {
		return createImage(new HexInputStream(hexData));
	}

/* Static method: loadAudioClip(name) */
/**
 * Searches the default audio clip search path for an audio clip with the specified
 * name and then loads it to create an <code>AudioClip</code>.  The search process
 * consists of the following steps:
 *
 * <p><ol>
 * <li>Check to see if an audio clip with that name has already been defined.  If
 *     so, return that audio clip.<p>
 *
 * <li>Check to see if there is a resource available with that name whose
 *     contents can be read as an <code>AudioClip</code>.  If so, read the
 *     audio clip from the resource file.<p>
 *
 * <li>Load the audio clip from a file with the specified name, relative to the
 *     application directory or the applet code base.
 * </ol><p>
 *
 * The second and third steps are repeated for each element of the audio clip
 * search path, which consists of a list of directories separated by colons.
 *
 * @usage AudioClip clip = MediaTools.loadAudioClip(name);
 * @param name The name of the audio clip
 * @return A new <code>AudioClip</code> object
 */
	public static AudioClip loadAudioClip(String name) {
		return loadAudioClip(name, DEFAULT_AUDIO_PATH);
	}

/* Static method: loadAudioClip(name, path) */
/**
 * Searches for an audio clip with the given name and loads it to create an
 * <code>AudioClip</code>.  Its operation is identical to the single-argument
 * <a href="#loadAudioClip(String)"><code>loadAudioClip</code></a> call except
 * in that this version allows the client to specify the search path
 * explicitly.
 *
 * @usage AudioClip clip = MediaTools.loadAudioClip(name, path);
 * @param name The name of the audio clip
 * @param path A string of directories names separated by colons
 * @return A fully loaded <code>AudioClip</code> object
 * @noshow
 */
	public static AudioClip loadAudioClip(String name, String path) {
		AudioClip clip = (AudioClip) audioClipTable.get(name);
		if (clip != null) return clip;
		if (name.endsWith(".au")) {
			try {
				Class soundClass = Class.forName("sounds." + name.substring(0, name.length() - 3));
				Object compiledAudioClip = soundClass.newInstance();
				Method getAudioClip = soundClass.getMethod("getAudioClip", new Class[0]);
				return (AudioClip) getAudioClip.invoke(compiledAudioClip, new Object[0]);
			} catch (Exception ex) {
				/* Empty */
			}
		}
		StringTokenizer tokenizer = new StringTokenizer(path, ":");
		while (clip == null && tokenizer.hasMoreTokens()) {
			String prefix = tokenizer.nextToken();
			prefix = (prefix.equals(".")) ? "" : prefix + "/";
			URL url = null;
			boolean resourceFlag = false;
			try {
				url = RESOURCE_CLASS.getResource("/" + prefix + name);
				URLConnection connection = url.openConnection();
				if (connection == null || connection.getContentLength() <= 0) {
					url = null;
				} else {
					resourceFlag = true;
				}
			} catch (Exception ex) {
				/* Empty */
			}
			if (url == null) {
				Applet applet = JTFTools.getApplet();
				if (applet != null) {
					URL codeBase = applet.getCodeBase();
					if (codeBase != null) {
						try {
							url = new URL(codeBase, prefix + name);
						} catch (MalformedURLException ex) {
							/* Empty */
						}
					}
				}
			}
			if (url == null) {
				try {
					File file = new File(prefix + name);
					if (file.canRead()) {
						clip = createAudioClip(new FileInputStream(file));
					}
				} catch (Exception ex) {
					/* Empty */
				}
			} else {
				try {
					URLConnection connection = url.openConnection();
					if (resourceFlag || connection.getContentLength() > 0) {
						Object content = connection.getContent();
						if (content instanceof AudioClip) {
							clip = (AudioClip) content;
						} else if (content instanceof InputStream) {
							clip = createAudioClip((InputStream) content);
						}
					}
				} catch (IOException ex) {
					/* Empty */
				}
			}
		}
		if (clip == null) throw new ErrorException("Cannot find an audio clip named " + name);
		if (cachingEnabled) audioClipTable.put(name, clip);
		return clip;
	}

/* Static method: defineAudioClip(name, clip) */
/**
 * Inserts the given clip into the audio clip table under the specified name.
 *
 * @usage MediaTools.defineAudioClip(clip);
 * @param name The name for the audio clip
 * @param clip The audio clip to be stored in the table
 */
	public static void defineAudioClip(String name, AudioClip clip) {
		audioClipTable.put(name, clip);
	}

/* Static method: flushAudioClip(name) */
/**
 * Removes the audio clip with the given name from the cache, allowing it
 * to be freed by the garbage collector.
 *
 * @usage MediaTools.flushAudioClip(name);
 * @param name The name for the audio clip
 */
	public static void flushAudioClip(String name) {
		audioClipTable.remove(name);
	}

/* Static method: createAudioClip(in) */
/**
 * Generates an audio clip from an input stream containing the data bytes for the
 * audio clip.
 *
 * @usage AudioClip clip = MediaTools.createAudioClip(in);
 * @param in An input stream containing the data
 * @return An <code>AudioClip</code> object
 */
	public static AudioClip createAudioClip(InputStream in) {
		try {
			return new SunAudioClip(in);
		} catch (Exception ex) {
			return new NullAudioClip();
		}
	}

/* Static method: createAudioClip(hexData) */
/**
 * Generates an audio clip from a string array that provides the pixel values.
 *
 * @usage AudioClip audio clip = MediaTools.createAudioClip(hexData);
 * @param hexData A hex string array representing an audio clip
 * @return An <code>AudioClip</code> object
 */
	public static AudioClip createAudioClip(String[] hexData) {
		return createAudioClip(new HexInputStream(hexData));
	}

/* Static method: getHexInputStream(hexData) */
/**
 * Returns an input stream whose bytes come from the string array <code>hex</code>,
 * in which the elements consist of continuous bytes of hex data.
 *
 * @usage InputStream = MediaTools.getHexInputStream(hexData);
 * @param hexData An array of strings specifying a byte stream coded in hex
 * @return An input stream for reading the bytes
 */
	public static InputStream getHexInputStream(String[] hexData) {
		return new HexInputStream(hexData);
	}

/* Static method: setCachingEnabled(boolean flag) */
/**
 * This method sets an internal flag in the <code>MediaTools</code> package to indicate
 * whether images and audio clips are cached internally by name.  This flag is enabled
 * by default, which gives better performance when clients load the same named image
 * more than once.  It does, however, tie up memory, making it unavailable to the
 * garbage collector.  Applications that make extensive use of memory should disable
 * this flag by calling <code>setCachingEnabled(false)</code>.
 *
 * @usage MediaTools.setCachingEnabled(flag);
 * @param flag <code>true</code> to enable caching by name, <code>false</code> to disable it.
 */
	public static void setCachingEnabled(boolean flag) {
		cachingEnabled = flag;
	}

/* Static method: isCachingEnabled() */
/**
 * This method returns the status of the flag that determins whether images and audio
 * clips are cached internally by name, as described in
 * <a href="#setCachingEnabled(boolean)"><code>setCachingEnabled</code></a>.
 *
 * @usage boolean flag = MediaTools.isCachingEnabled();
 * @return <code>true</code> if caching by name is enabled, <code>false</code> otherwise.
 */
	public static boolean isCachingEnabled() {
		return cachingEnabled;
	}

/* Static method: getImageObserver() */
/**
 * This method returns a new lightweight component suitable as an <code>imageObserver</code>.
 *
 * @usage Component imageObserver = MediaTools.getImageObserver();
 * @return A new lightweight component suitable as an <code>imageObserver</code>.
 */
	public static Component getImageObserver() {
		return JTFTools.createEmptyContainer();
	}

/* Static method: beep() */
/**
 * This method sounds the audible alert on the console, which is typically a beep
 * sound.
 *
 * @usage MediaTools.beep();
 */
	public static void beep() {
		Toolkit.getDefaultToolkit().beep();
	}

/* Private instance variables */

	private static boolean cachingEnabled = true;
	private static HashMap imageTable = new HashMap();
	private static HashMap audioClipTable = new HashMap();
	private static final Class RESOURCE_CLASS = new MediaTools().getClass();
}

/* Package class: HexInputStream */

/**
 * This class defines an <code>InputStream</code> whose bytes come from a string
 * of hexadecimal digits.
 */
class HexInputStream extends InputStream {

/* Constructor: HexInputStream(hexData) */
/**
 * Creates an input stream whose bytes come from the specified string array.
 *
 * @usage HexInputStream in = new HexInputStream(hexData);
 * @param hexData An array of strings containing the input bytes
 */
	public HexInputStream(String[] hexData) {
		hex = hexData;
		arrayIndex = 0;
		charIndex = 0;
	}

/* Method: read() */
/**
 * Reads the next byte of data from the input.
 *
 * @usage int ch = in.read();
 * @return The next byte of data, or -1 at the end of file
 */
	public int read() {
		if (arrayIndex >= hex.length) return -1;
		if (charIndex >= hex[arrayIndex].length()) {
			arrayIndex++;
			charIndex = 0;
			return read();
		}
		int data = Character.digit(hex[arrayIndex].charAt(charIndex++), 16) << 4;
		data |= Character.digit(hex[arrayIndex].charAt(charIndex++), 16);
		return data;
	}

/* Private instance variables */

	private String[] hex;
	private int arrayIndex;
	private int charIndex;

}

/* Package class: SunAudioClip */

/**
 * This class implements the AudioClip interface in terms of the
 * <code>sun.audio</code> package, which appears to be supported
 * in all major browsers.  The clip is created through reflection
 * in order to avoid a compile-time dependency on the sources for
 * <code>sun.audio</code>.
 */
class SunAudioClip implements AudioClip {

/* Constructor: SunAudioClip(in) */
/**
 * Creates an audio clip from the specified input stream using the
 * <code>sun.audio</code> package.  The audio clip is fully loaded
 * at the point the constructor returns.  This class ignores all
 * exceptions, which means that sounds will simply not play on
 * systems on which this facility is not supported.
 */
	public SunAudioClip(InputStream in) {
		if (!initialized) {
			initStaticData();
			initialized = true;
		}
		try {
			Object[] args = { in };
			Object audioStream = audioDataConstructor.newInstance(args);
			audioData = getData.invoke(audioStream, new Object[0]);
			player = audioPlayerClass.getField("player").get(null);
			Class[] inputStreamTypes = { Class.forName("java.io.InputStream") };
			audioPlayerStart = player.getClass().getMethod("start", inputStreamTypes);
			audioPlayerStop = player.getClass().getMethod("stop", inputStreamTypes);
		} catch (Exception ex) {
			/* Ignore any exceptions */
		}
	}

	public void play() {
		try {
			Object[] args = { audioData };
			audioDataStream = audioDataStreamConstructor.newInstance(args);
			args[0] = audioDataStream;
			audioPlayerStart.invoke(player, args);
		} catch (Exception ex) {
			/* Ignore any exceptions */
		}
	}

	public void loop() {
		try {
			Object[] args = { audioData };
			continuousAudioDataStream = continuousAudioDataStreamConstructor.newInstance(args);
			args[0] = continuousAudioDataStream;
			audioPlayerStart.invoke(player, args);
		} catch (Exception ex) {
			/* Ignore any exceptions */
		}
	}

	public void stop() {
		try {
			Object[] args = new Object[1];
			if (continuousAudioDataStream != null) {
				args[0] = audioDataStream;
				audioPlayerStop.invoke(player, args);
			}
			if (audioDataStream != null) {
				args[0] = continuousAudioDataStream;
				audioPlayerStop.invoke(player, args);
			}
		} catch (Exception ex) {
			/* Ignore any exceptions */
		}
	}

	private static void initStaticData() {
		try {
			audioPlayerClass = Class.forName("sun.audio.AudioPlayer");
			audioStreamClass = Class.forName("sun.audio.AudioStream");
			audioDataClass = Class.forName("sun.audio.AudioData");
			audioDataStreamClass = Class.forName("sun.audio.AudioDataStream");
			continuousAudioDataStreamClass = Class.forName("sun.audio.ContinuousAudioDataStream");
			Class[] inputStreamTypes = { Class.forName("java.io.InputStream") };
			audioDataConstructor = audioStreamClass.getConstructor(inputStreamTypes);
			getData = audioStreamClass.getMethod("getData", new Class[0]);
			Class[] audioDataTypes = { audioDataClass };
			audioDataStreamConstructor = audioDataStreamClass.getConstructor(audioDataTypes);
			continuousAudioDataStreamConstructor = continuousAudioDataStreamClass.getConstructor(audioDataTypes);
		} catch (Exception ex) {
			/* Ignore any exceptions */
		}
	}

/* Private instance variables */

	private static boolean initialized;
	private static Class audioPlayerClass;
	private static Class audioStreamClass;
	private static Class audioDataClass;
	private static Class audioDataStreamClass;
	private static Class continuousAudioDataStreamClass;
	private static Constructor audioDataConstructor;
	private static Constructor audioDataStreamConstructor;
	private static Constructor continuousAudioDataStreamConstructor;
	private static Method getData;

	private Object player;
	private Object audioData;
	private Object audioDataStream;
	private Object continuousAudioDataStream;
	private Method audioPlayerStart;
	private Method audioPlayerStop;

}

/* Package class: NullAudioClip */

/**
 * This class implements the AudioClip interface with a stub that
 * ignores all of the calls.
 */
class NullAudioClip implements AudioClip {

	public void play() {
		/* Empty */
	}

	public void loop() {
		/* Empty */
	}

	public void stop() {
		/* Empty */
	}

}
