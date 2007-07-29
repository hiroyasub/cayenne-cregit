begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|reflect
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|ObjectId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|PersistentObject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|EntityResolver
import|;
end_import

begin_class
specifier|public
class|class
name|LifecycleCallbackEventHandlerTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testDefaultListeners
parameter_list|()
block|{
name|LifecycleCallbackEventHandler
name|map
init|=
operator|new
name|LifecycleCallbackEventHandler
argument_list|(
operator|new
name|EntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|L1
name|l1
init|=
operator|new
name|L1
argument_list|()
decl_stmt|;
name|map
operator|.
name|addDefaultListener
argument_list|(
name|l1
argument_list|,
literal|"callback"
argument_list|)
expr_stmt|;
name|C1
name|c1
init|=
operator|new
name|C1
argument_list|()
decl_stmt|;
name|c1
operator|.
name|setObjectId
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"bogus"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|l1
operator|.
name|entities
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|performCallbacks
argument_list|(
name|c1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|l1
operator|.
name|entities
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|l1
operator|.
name|entities
operator|.
name|contains
argument_list|(
name|c1
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDefaultListenersCallbackOrder
parameter_list|()
block|{
name|LifecycleCallbackEventHandler
name|map
init|=
operator|new
name|LifecycleCallbackEventHandler
argument_list|(
operator|new
name|EntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|L2
name|l1
init|=
operator|new
name|L2
argument_list|()
decl_stmt|;
name|map
operator|.
name|addListener
argument_list|(
name|C1
operator|.
name|class
argument_list|,
name|l1
argument_list|,
literal|"callback"
argument_list|)
expr_stmt|;
name|L2
name|l2
init|=
operator|new
name|L2
argument_list|()
decl_stmt|;
name|map
operator|.
name|addDefaultListener
argument_list|(
name|l2
argument_list|,
literal|"callback"
argument_list|)
expr_stmt|;
name|C1
name|c1
init|=
operator|new
name|C1
argument_list|()
decl_stmt|;
name|c1
operator|.
name|setObjectId
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"bogus"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|performCallbacks
argument_list|(
name|c1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|l1
operator|.
name|callbackTimes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|l2
operator|.
name|callbackTimes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Long
name|t1
init|=
operator|(
name|Long
operator|)
name|l1
operator|.
name|callbackTimes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Long
name|t2
init|=
operator|(
name|Long
operator|)
name|l2
operator|.
name|callbackTimes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|t2
operator|.
name|compareTo
argument_list|(
name|t1
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCallbackOnSuperclass
parameter_list|()
block|{
name|LifecycleCallbackEventHandler
name|map
init|=
operator|new
name|LifecycleCallbackEventHandler
argument_list|(
operator|new
name|EntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|map
operator|.
name|addListener
argument_list|(
name|C1
operator|.
name|class
argument_list|,
literal|"c1Callback"
argument_list|)
expr_stmt|;
name|C3
name|subclass
init|=
operator|new
name|C3
argument_list|()
decl_stmt|;
name|subclass
operator|.
name|setObjectId
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"bogusSubclass"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|subclass
operator|.
name|callbacks
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|performCallbacks
argument_list|(
name|subclass
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|subclass
operator|.
name|callbacks
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCallbackOnSuperclassWithSublcassOverrides
parameter_list|()
block|{
name|LifecycleCallbackEventHandler
name|map
init|=
operator|new
name|LifecycleCallbackEventHandler
argument_list|(
operator|new
name|EntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|map
operator|.
name|addListener
argument_list|(
name|C1
operator|.
name|class
argument_list|,
literal|"c1Callback"
argument_list|)
expr_stmt|;
name|C4
name|subclass
init|=
operator|new
name|C4
argument_list|()
decl_stmt|;
name|subclass
operator|.
name|setObjectId
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"bogus"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|subclass
operator|.
name|callbacks
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|performCallbacks
argument_list|(
name|subclass
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|subclass
operator|.
name|callbacks
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"c4Callback"
argument_list|,
name|subclass
operator|.
name|callbacks
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCallbackOrderInInheritanceHierarchy
parameter_list|()
block|{
name|LifecycleCallbackEventHandler
name|map
init|=
operator|new
name|LifecycleCallbackEventHandler
argument_list|(
operator|new
name|EntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|map
operator|.
name|addListener
argument_list|(
name|C2
operator|.
name|class
argument_list|,
literal|"c2Callback"
argument_list|)
expr_stmt|;
name|map
operator|.
name|addListener
argument_list|(
name|C1
operator|.
name|class
argument_list|,
literal|"c1Callback"
argument_list|)
expr_stmt|;
name|C2
name|c
init|=
operator|new
name|C2
argument_list|()
decl_stmt|;
name|c
operator|.
name|setObjectId
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"bogus"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|c
operator|.
name|callbacks
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|performCallbacks
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|c
operator|.
name|callbacks
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// superclass callbacks should be invoked first
name|assertEquals
argument_list|(
literal|"c1Callback"
argument_list|,
name|c
operator|.
name|callbacks
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"c2Callback"
argument_list|,
name|c
operator|.
name|callbacks
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|static
class|class
name|C1
extends|extends
name|PersistentObject
block|{
specifier|protected
name|List
name|callbacks
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|void
name|c1Callback
parameter_list|()
block|{
name|callbacks
operator|.
name|add
argument_list|(
literal|"c1Callback"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|C2
extends|extends
name|C1
block|{
name|void
name|c2Callback
parameter_list|()
block|{
name|callbacks
operator|.
name|add
argument_list|(
literal|"c2Callback"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|C3
extends|extends
name|C1
block|{      }
specifier|static
class|class
name|C4
extends|extends
name|C1
block|{
name|void
name|c1Callback
parameter_list|()
block|{
name|callbacks
operator|.
name|add
argument_list|(
literal|"c4Callback"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|L1
block|{
specifier|protected
name|List
name|entities
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|void
name|callback
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
name|entities
operator|.
name|add
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|L2
block|{
specifier|protected
name|List
name|callbackTimes
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|void
name|callback
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
name|callbackTimes
operator|.
name|add
argument_list|(
operator|new
name|Long
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

