begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|///*****************************************************************
end_comment

begin_comment
comment|// *   Licensed to the Apache Software Foundation (ASF) under one
end_comment

begin_comment
comment|// *  or more contributor license agreements.  See the NOTICE file
end_comment

begin_comment
comment|// *  distributed with this work for additional information
end_comment

begin_comment
comment|// *  regarding copyright ownership.  The ASF licenses this file
end_comment

begin_comment
comment|// *  to you under the Apache License, Version 2.0 (the
end_comment

begin_comment
comment|// *  "License"); you may not use this file except in compliance
end_comment

begin_comment
comment|// *  with the License.  You may obtain a copy of the License at
end_comment

begin_comment
comment|// *
end_comment

begin_comment
comment|// *    http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|// *
end_comment

begin_comment
comment|// *  Unless required by applicable law or agreed to in writing,
end_comment

begin_comment
comment|// *  software distributed under the License is distributed on an
end_comment

begin_comment
comment|// *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
end_comment

begin_comment
comment|// *  KIND, either express or implied.  See the License for the
end_comment

begin_comment
comment|// *  specific language governing permissions and limitations
end_comment

begin_comment
comment|// *  under the License.
end_comment

begin_comment
comment|// ****************************************************************/
end_comment

begin_comment
comment|//package org.apache.cayenne.web;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//import com.mockrunner.mock.web.MockHttpServletRequest;
end_comment

begin_comment
comment|//import com.mockrunner.mock.web.MockHttpServletResponse;
end_comment

begin_comment
comment|//import com.mockrunner.mock.web.MockHttpSession;
end_comment

begin_comment
comment|//import org.apache.cayenne.BaseContext;
end_comment

begin_comment
comment|//import org.apache.cayenne.DataChannel;
end_comment

begin_comment
comment|//import org.apache.cayenne.MockDataChannel;
end_comment

begin_comment
comment|//import org.apache.cayenne.ObjectContext;
end_comment

begin_comment
comment|//import org.apache.cayenne.configuration.ObjectContextFactory;
end_comment

begin_comment
comment|//import org.apache.cayenne.di.DIBootstrap;
end_comment

begin_comment
comment|//import org.apache.cayenne.di.Injector;
end_comment

begin_comment
comment|//import org.apache.cayenne.di.Module;
end_comment

begin_comment
comment|//import org.junit.Test;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//import static org.junit.Assert.*;
end_comment

begin_comment
comment|//import static org.mockito.Mockito.mock;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//public class ServletContextHandlerTest {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    @Test
end_comment

begin_comment
comment|//    public void testRequestStart_bindContext() {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        Module module = binder -> {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            binder.bind(DataChannel.class).to(MockDataChannel.class);
end_comment

begin_comment
comment|//            binder.bind(ObjectContextFactory.class).toInstance(
end_comment

begin_comment
comment|//                    new ObjectContextFactory() {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//                        public ObjectContext createContext(DataChannel parent) {
end_comment

begin_comment
comment|//                            return mock(ObjectContext.class);
end_comment

begin_comment
comment|//                        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//                        public ObjectContext createContext() {
end_comment

begin_comment
comment|//                            return mock(ObjectContext.class);
end_comment

begin_comment
comment|//                        }
end_comment

begin_comment
comment|//                    });
end_comment

begin_comment
comment|//        };
end_comment

begin_comment
comment|//        Injector injector = DIBootstrap.createInjector(module);
end_comment

begin_comment
comment|//        SessionContextRequestHandler handler = new SessionContextRequestHandler();
end_comment

begin_comment
comment|//        injector.injectMembers(handler);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        MockHttpSession session = new MockHttpSession();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        BaseContext.bindThreadObjectContext(null);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        try {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            MockHttpServletRequest request1 = new MockHttpServletRequest();
end_comment

begin_comment
comment|//            MockHttpServletResponse response1 = new MockHttpServletResponse();
end_comment

begin_comment
comment|//            request1.setSession(session);
end_comment

begin_comment
comment|//            handler.requestStart(request1, response1);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            ObjectContext c1 = BaseContext.getThreadObjectContext();
end_comment

begin_comment
comment|//            assertNotNull(c1);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            handler.requestEnd(request1, response1);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            try {
end_comment

begin_comment
comment|//                BaseContext.getThreadObjectContext();
end_comment

begin_comment
comment|//                fail("thread context not null");
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//            catch (IllegalStateException e) {
end_comment

begin_comment
comment|//                // expected
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            MockHttpServletRequest request2 = new MockHttpServletRequest();
end_comment

begin_comment
comment|//            MockHttpServletResponse response2 = new MockHttpServletResponse();
end_comment

begin_comment
comment|//            request2.setSession(session);
end_comment

begin_comment
comment|//            handler.requestStart(request2, response2);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            ObjectContext c2 = BaseContext.getThreadObjectContext();
end_comment

begin_comment
comment|//            assertSame(c1, c2);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            handler.requestEnd(request2, response2);
end_comment

begin_comment
comment|//            try {
end_comment

begin_comment
comment|//                BaseContext.getThreadObjectContext();
end_comment

begin_comment
comment|//                fail("thread context not null");
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//            catch (IllegalStateException e) {
end_comment

begin_comment
comment|//                // expected
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            MockHttpServletRequest request3 = new MockHttpServletRequest();
end_comment

begin_comment
comment|//            MockHttpServletResponse response3 = new MockHttpServletResponse();
end_comment

begin_comment
comment|//            request3.setSession(new MockHttpSession());
end_comment

begin_comment
comment|//            handler.requestStart(request3, response3);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            ObjectContext c3 = BaseContext.getThreadObjectContext();
end_comment

begin_comment
comment|//            assertNotNull(c3);
end_comment

begin_comment
comment|//            assertNotSame(c1, c3);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            handler.requestEnd(request3, response3);
end_comment

begin_comment
comment|//            try {
end_comment

begin_comment
comment|//                BaseContext.getThreadObjectContext();
end_comment

begin_comment
comment|//                fail("thread context not null");
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//            catch (IllegalStateException e) {
end_comment

begin_comment
comment|//                // expected
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//        finally {
end_comment

begin_comment
comment|//            BaseContext.bindThreadObjectContext(null);
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//}
end_comment

end_unit

