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

import android.util.Log;

import org.apache.mina.core.polling.AbstractPollingIoConnector;
import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.core.service.TransportMetadata;
import org.apache.mina.socket.DefaultSocketSessionConfig;
import org.apache.mina.socket.SocketConnector;
import org.apache.mina.socket.SocketSessionConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Executor;

/**
 * {@link org.apache.mina.core.service.IoConnector} for socket transport (TCP/IP).
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public final class NioSocketConnector extends AbstractPollingIoConnector<NioSession, SocketChannel> implements
        SocketConnector {

    private volatile Selector selector;

    /**
     * Constructor for {@link NioSocketConnector} with default configuration (multiple thread model).
     */
    public NioSocketConnector() {
        super(new DefaultSocketSessionConfig(), NioProcessor.class);
        ((DefaultSocketSessionConfig) getSessionConfig()).init(this);
    }

    /**
     * Constructor for {@link NioSocketConnector} with default configuration, and
     * given number of {@link NioProcessor} for multithreading I/O operations
     * @param processorCount the number of processor to create and place in a
     * {@link org.apache.mina.core.service.SimpleIoProcessorPool}
     */
    public NioSocketConnector(int processorCount) {
        super(new DefaultSocketSessionConfig(), NioProcessor.class, processorCount);
        ((DefaultSocketSessionConfig) getSessionConfig()).init(this);
    }

    /**
     *  Constructor for {@link NioSocketConnector} with default configuration but a
     *  specific {@link org.apache.mina.core.service.IoProcessor}, useful for sharing the same processor over multiple
     *  {@link org.apache.mina.core.service.IoService} of the same type.
     * @param processor the processor to use for managing I/O events
     */
    public NioSocketConnector(IoProcessor<NioSession> processor) {
        super(new DefaultSocketSessionConfig(), processor);
        ((DefaultSocketSessionConfig) getSessionConfig()).init(this);
    }

    /**
     *  Constructor for {@link NioSocketConnector} with a given {@link java.util.concurrent.Executor} for handling
     *  connection events and a given {@link org.apache.mina.core.service.IoProcessor} for handling I/O events, useful for sharing
     *  the same processor and executor over multiple {@link org.apache.mina.core.service.IoService} of the same type.
     * @param executor the executor for connection
     * @param processor the processor for I/O operations
     */
    public NioSocketConnector(Executor executor, IoProcessor<NioSession> processor) {
        super(new DefaultSocketSessionConfig(), executor, processor);
        ((DefaultSocketSessionConfig) getSessionConfig()).init(this);
    }

    /**
     * Constructor for {@link NioSocketConnector} with default configuration which will use a built-in
     * thread pool executor to manage the given number of processor instances. The processor class must have
     * a constructor that accepts ExecutorService or Executor as its single argument, or, failing that, a
     * no-arg constructor.
     *
     * @param processorClass the processor class.
     * @param processorCount the number of processors to instantiate.
     * @see org.apache.mina.core.service.SimpleIoProcessorPool#SimpleIoProcessorPool(Class, java.util.concurrent.Executor, int, java.nio.channels.spi.SelectorProvider)
     * @since 2.0.0-M4
     */
    public NioSocketConnector(Class<? extends IoProcessor<NioSession>> processorClass, int processorCount) {
        super(new DefaultSocketSessionConfig(), processorClass, processorCount);
    }

    /**
     * Constructor for {@link NioSocketConnector} with default configuration with default configuration which will use a built-in
     * thread pool executor to manage the default number of processor instances. The processor class must have
     * a constructor that accepts ExecutorService or Executor as its single argument, or, failing that, a
     * no-arg constructor. The default number of instances is equal to the number of processor cores
     * in the system, plus one.
     *
     * @param processorClass the processor class.
     * @see org.apache.mina.core.service.SimpleIoProcessorPool#SimpleIoProcessorPool(Class, java.util.concurrent.Executor, int, java.nio.channels.spi.SelectorProvider)
     * @see org.apache.mina.core.service.SimpleIoProcessorPool#DEFAULT_SIZE
     * @since 2.0.0-M4
     */
    public NioSocketConnector(Class<? extends IoProcessor<NioSession>> processorClass) {
        super(new DefaultSocketSessionConfig(), processorClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void init() throws Exception {
        this.selector = Selector.open();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void destroy() throws Exception {
        if (selector != null) {
            selector.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    public TransportMetadata getTransportMetadata() {
        return NioSocketSession.METADATA;
    }

    /**
     * {@inheritDoc}
     */
    public SocketSessionConfig getSessionConfig() {
        return (SocketSessionConfig) sessionConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InetSocketAddress getDefaultRemoteAddress() {
        return (InetSocketAddress) super.getDefaultRemoteAddress();
    }

    /**
     * {@inheritDoc}
     */
    public void setDefaultRemoteAddress(InetSocketAddress defaultRemoteAddress) {
        super.setDefaultRemoteAddress(defaultRemoteAddress);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Iterator<SocketChannel> allHandles() {
        return new SocketChannelIterator(selector.keys());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean connect(SocketChannel handle, SocketAddress remoteAddress) throws Exception {
        Log.i(this.getClass().getSimpleName(), "connect start"+android.os.Process.myPid()+'-'+android.os.Process.myTid());
        return handle.connect(remoteAddress);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ConnectionRequest getConnectionRequest(SocketChannel handle) {
        SelectionKey key = handle.keyFor(selector);

        if ((key == null) || (!key.isValid())) {
            return null;
        }

        return (ConnectionRequest) key.attachment();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void close(SocketChannel handle) throws Exception {
        SelectionKey key = handle.keyFor(selector);

        if (key != null) {
            key.cancel();
        }

        handle.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean finishConnect(SocketChannel handle) throws Exception {
        if (handle.finishConnect()) {
            SelectionKey key = handle.keyFor(selector);

            if (key != null) {
                key.cancel();
            }

            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SocketChannel newHandle(SocketAddress localAddress) throws Exception {
        SocketChannel ch = SocketChannel.open();

        int receiveBufferSize = (getSessionConfig()).getReceiveBufferSize();

        if (receiveBufferSize > 65535) {
            ch.socket().setReceiveBufferSize(receiveBufferSize);
        }

        if (localAddress != null) {
            try {
                ch.socket().bind(localAddress);
            } catch (IOException ioe) {
                // Add some info regarding the address we try to bind to the
                // message
                String newMessage = "Error while binding on " + localAddress + "\n" + "original message : "
                        + ioe.getMessage();
                Exception e = new IOException(newMessage);
                e.initCause(ioe.getCause());

                // Preemptively close the channel
                ch.close();
                throw ioe;
            }
        }

        ch.configureBlocking(false);

        return ch;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected NioSession newSession(IoProcessor<NioSession> processor, SocketChannel handle) {
        return new NioSocketSession(this, processor, handle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void register(SocketChannel handle, ConnectionRequest request) throws Exception {
        handle.register(selector, SelectionKey.OP_CONNECT, request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int select(int timeout) throws Exception {
        return selector.select(timeout);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Iterator<SocketChannel> selectedHandles() {
        return new SocketChannelIterator(selector.selectedKeys());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void wakeup() {
        selector.wakeup();
    }

    private static class SocketChannelIterator implements Iterator<SocketChannel> {

        private final Iterator<SelectionKey> i;

        private SocketChannelIterator(Collection<SelectionKey> selectedKeys) {
            this.i = selectedKeys.iterator();
        }

        /**
         * {@inheritDoc}
         */
        public boolean hasNext() {
            return i.hasNext();
        }

        /**
         * {@inheritDoc}
         */
        public SocketChannel next() {
            SelectionKey key = i.next();
            return (SocketChannel) key.channel();
        }

        /**
         * {@inheritDoc}
         */
        public void remove() {
            i.remove();
        }
    }
}
