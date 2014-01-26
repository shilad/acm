package edu.macalester;

import acm.graphics.GLabel;
import acm.program.GraphicsProgram;

/**
 * @author Shilad Sen
 */
public class Test extends GraphicsProgram {
    @Override
    public void run() {
        add(new GLabel("Hello, World!"));
    }
}
