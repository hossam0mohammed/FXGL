/*
 * The MIT License (MIT)
 *
 * FXGL - JavaFX Game Library
 *
 * Copyright (c) 2015 AlmasB (almaslvl@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.almasb.fxgl.effect;

import com.almasb.fxgl.time.TimerManager;

import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public final class FireEmitter extends ParticleEmitter {

    private Paint color = Color.rgb(230, 75, 40);

    public FireEmitter() {
        setNumParticles(15);
        setEmissionRate(0.5);
    }

    public void setColor(Paint color) {
        this.color = color;
    }

    @Override
    protected Particle emit(int i, double x, double y) {
        Point2D spawn = new Point2D(i * (rand() - 0.5), (rand() - 1));
        Particle p = new Particle(new Point2D(x, y).add(spawn),
                new Point2D(rand(-0.5, 0.5) * 0.25, rand() * -1),
                new Point2D(0, 0),
                7 + rand(2, 5),
                new Point2D(rand(-0.01, 0.01) * 10, rand() * -0.1),
                TimerManager.toNanos(1),
                color,
                i < getNumParticles() / 2 ? BlendMode.ADD : BlendMode.COLOR_DODGE);
        return p;
    }
}