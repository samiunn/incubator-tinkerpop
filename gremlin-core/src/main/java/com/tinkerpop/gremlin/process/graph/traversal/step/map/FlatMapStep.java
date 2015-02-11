/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.tinkerpop.gremlin.process.graph.traversal.step.map;

import com.tinkerpop.gremlin.process.Traversal;
import com.tinkerpop.gremlin.process.Traverser;
import com.tinkerpop.gremlin.process.traversal.step.AbstractStep;

import java.util.Collections;
import java.util.Iterator;
import java.util.function.Function;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FlatMapStep<S, E> extends AbstractStep<S, E> {

    private Function<Traverser<S>, Iterator<E>> function = null;
    private Traverser.Admin<S> head = null;
    private Iterator<E> iterator = Collections.emptyIterator();

    public FlatMapStep(final Traversal.Admin traversal) {
        super(traversal);
    }

    public void setFunction(final Function<Traverser<S>, Iterator<E>> function) {
        this.function = function;
    }

    @Override
    protected Traverser<E> processNextStart() {
        while (true) {
            if (this.iterator.hasNext()) {
                return this.head.split(this.iterator.next(), this);
            } else {
                this.head = this.starts.next();
                this.iterator = this.function.apply(this.head);
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.iterator = Collections.emptyIterator();
    }
}