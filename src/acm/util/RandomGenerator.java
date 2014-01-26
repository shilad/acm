/*
 * @(#)RandomGenerator.java   1.0 06/08/25
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

import java.awt.*;
import java.util.Random;

/* Class: RandomGenerator */

/**
 * This class implements a simple random number generator that allows
 * clients to generate pseudorandom integers, doubles, booleans, and
 * colors.  To use it, the first step is to declare an instance variable
 * to hold the random generator as follows:
 *
 * <p><code>&nbsp;&nbsp;&nbsp;private RandomGenerator rgen = RandomGenerator.getInstance();</code>
 *
 * <p>When you then need to generate a random value, you call the appropriate
 * method on the <code>rgen</code> variable.
 *
 * <p>The <code>RandomGenerator</code> class is actually implemented
 * as an extension to the <code>Random</code> class in <code>java.util</code>.
 * The new version has the following advantages over the original:
 *
 * <p><ul>
 * <li>The name of the class emphasizes that the object is a random
 *     <u>generator</u> rather than a random value.<p>
 * <li>The class includes overloaded versions of <code>nextInt</code>
 *     and <code>nextDouble</code> to simplify choosing numbers in
 *     a specific range.<p>
 * <li>The method <code>nextBoolean</code> is overloaded to allow
 *     the specification of a probability.<p>
 * <li>The class includes a method <code>nextColor</code> that
 *     generates a random opaque color.<p>
 * </ul><p>
 */
public class RandomGenerator extends Random {

/* Constructor: RandomGenerator() */
/**
 * Creates a new random generator initialized to an unpredictable starting
 * point.  In almost all programs, you want to call this method <u>once</u>
 * to produce a single generator object, which you then use each time you
 * need to generate a random value.  If you create several random generators
 * in succession, they will typically generate the same sequence of values.
 *
 * <p>During debugging, it is often useful to set the internal seed for
 * the random generator explicitly so that it always returns the same sequence.
 * To do so, you need to invoke the
 * <a href="http://com/j2se/1.4.2/docs/api/java/util/Random.html#setSeed(int)"><code>setSeed</code></a>
 * method.
 *
 * @usage RandomGenerator rgen = new RandomGenerator();
 */
	public RandomGenerator() {
		/* Empty */
	}

/* Method: nextInt(n) */
/**
 * Returns the next random integer between 0 and <code>n</code>-1, inclusive.
 * This method is in modern implementations of the <code>Random</code> class,
 * but is missing from JDK 1.1.
 * @noshow
 */
	public int nextInt(int n) {
		return nextInt(0, n - 1);
	}

/* Method: nextBoolean() */
/**
 * Returns a random <code>boolean</code> value that is <code>true</code> or
 * <code>false</code> with equal probability.  This method is in modern
 * implementations of the <code>Random</code> class, but is missing from JDK 1.1.
 * @noshow
 */
	public boolean nextBoolean() {
		return nextBoolean(0.5);
	}

/* Method: nextInt(low, high) */
/**
 * Returns the next random integer in the specified range.  For example, you
 * can generate the roll of a six-sided die by calling
 *
 * <p><code>&nbsp;&nbsp;&nbsp;rgen.nextInt(1, 6);</code>
 *
 * <p>or a random decimal digit by calling
 *
 * <p><code>&nbsp;&nbsp;&nbsp;rgen.nextInt(0, 9);</code>
 *
 * @usage int k = rgen.nextInt(low, high)
 * @param low The low end of the range
 * @param high The high end of the range
 * @return The next random <code>int</code> between <code>low</code> and <code>high</code>, inclusive
 */
	public int nextInt(int low, int high) {
		return low + (int) ((high - low + 1) * nextDouble());
	}

/* Method: nextDouble(low, high) */
/**
 * Returns the next random real number in the specified range.  The resulting value is
 * always at least <code>low</code> but always strictly less than <code>high</code>.
 * You can use this method to generate continuous random values.  For example, you
 * can set the variables <code>x</code> and <code>y</code> to specify a random
 * point inside the unit square as follows:
 *
 * <p><code>&nbsp;&nbsp;&nbsp;double x = rgen.nextDouble(0.0, 1.0);
 * <br>&nbsp;&nbsp;&nbsp;double y = rgen.nextDouble(0.0, 1.0);</code>
 *
 * @usage double d = rgen.nextDouble(low, high)
 * @param low The low end of the range
 * @param high The high end of the range
 * @return A random <code>double</code> value <i>d</i> in the range <code>low</code> &le; <i>d</i> &lt; <code>high</code>
 */
	public double nextDouble(double low, double high) {
		return low + (high - low) * nextDouble();
	}

/* Method: nextBoolean(probability) */
/**
 * Returns a random <code>boolean</code> value with the specified probability.  You can use
 * this method to simulate an event that occurs with a particular probability.  For example,
 * you could simulate the result of tossing a coin like this:
 *
 * <p><code>&nbsp;&nbsp;&nbsp;String coinFlip = rgen.nextBoolean(0.5) ? "HEADS" : "TAILS";</code>
 *
 * @usage if (rgen.nextBoolean(p)) . . .
 * @param p A value between 0 (impossible) and 1 (certain) indicating the probability
 * @return The value <code>true</code> with probability <code>p</code>
 */
	public boolean nextBoolean(double p) {
		return nextDouble() < p;
	}

/* Method: nextColor() */
/**
 * Returns a random opaque <a href="http://com/j2se/1.4.2/docs/api/java/awt/Color.html"
 * ><code>Color</code></a> whose components are chosen uniformly
 * in the 0-255 range.
 *
 * @usage Color color = rgen.newColor()
 * @return A random opaque <a href="http://com/j2se/1.4.2/docs/api/java/awt/Color.html"><code>Color</code></a>
 */
	public Color nextColor() {
		return new Color(nextInt(256), nextInt(256), nextInt(256));
	}

/* Static method: getInstance() */
/**
 * This method returns a <code>RandomGenerator</code> instance that can
 * be shared among several classes.
 *
 * @usage RandomGenerator rgen = RandomGenerator.getInstance();
 * @return A shared <code>RandomGenerator</code> object
 */
	public static RandomGenerator getInstance() {
		if (standardInstance == null) standardInstance = new RandomGenerator();
		return standardInstance;
	}

/* Inherited method: setSeed(seed) */
/**
 * @inherited Random#void setSeed(long seed)
 * Sets a new starting point for the random generator sequence.
 */

/* Inherited method: nextDouble() */
/**
 * @inherited Random#double nextDouble()
 * Returns a random double <i>d</i> in the range 0 &le; <i>d</i> &lt; 1.
 */

/* Inherited method: nextInt() */
/**
 * @inherited Random#int nextInt(int n)
 * Returns a random integer <i>k</i> in the range 0 &le; <i>k</i> &lt; <code>n</code>.
 */

/* Inherited method: nextBoolean() */
/**
 * @inherited Random#boolean nextBoolean()
 * Returns a random boolean that is true 50 percent of the time.
 */

/* Private static variables */
	private static RandomGenerator standardInstance;
}
