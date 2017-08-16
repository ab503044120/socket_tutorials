/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.apache.mina.socket.nio;

import org.apache.mina.core.filterchain.DefaultIoFilterChain;
import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.session.AbstractIoSession;

import java.nio.channels.ByteChannel;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;

/**
 * An {@link org.apache.mina.core.session.IoSession} which is managed by the NIO transport.
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public abstract class NioSession extends AbstractIoSession {
    /** The NioSession processor */
    protected final IoProcessor<NioSession> processor;

    /** The communication channel */
    protected final Channel channel;

    /** The SelectionKey used for this session */
    protected SelectionKey key;

    /** The FilterChain created for this session */
    private final IoFilterChain filterChain;

    /**
     *
     * Creates a new instance of NioSession, with its associated IoProcessor.
     * <br>
     * This method is only called by the inherited class.
     *
     * @param processor The associated IoProcessor
     */
    protected NioSession(IoProcessor<NioSession> processor, IoService service, Channel channel) {
        super(service);
        this.channel = channel;
        this.processor = processor;
        filterChain = new DefaultIoFilterChain(this);
    }

    /**
     * @return The ByteChannel associated with this {@link org.apache.mina.core.session.IoSession}
     */
    abstract ByteChannel getChannel();

    public IoFilterChain getFilterChain() {
        return filterChain;
    }

    /**
     * @return The {@link java.nio.channels.SelectionKey} associated with this {@link org.apache.mina.core.session.IoSession}
     */
    /* No qualifier*/SelectionKey getSelectionKey() {
        return key;
    }

    /**
     * Sets the {@link java.nio.channels.SelectionKey} for this {@link org.apache.mina.core.session.IoSession}
     *
     * @param key The new {@link java.nio.channels.SelectionKey}
     */
    /* No qualifier*/void setSelectionKey(SelectionKey key) {
        this.key = key;
    }

    /**
     * {@inheritDoc}
     */
    public IoProcessor<NioSession> getProcessor() {
        return processor;
    }
}
