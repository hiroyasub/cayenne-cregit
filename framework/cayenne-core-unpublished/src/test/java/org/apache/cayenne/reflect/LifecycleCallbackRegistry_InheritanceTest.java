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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|ObjectContext
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
name|Persistent
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
name|annotation
operator|.
name|PostAdd
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
name|di
operator|.
name|Inject
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
name|LifecycleEvent
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
name|testdo
operator|.
name|inheritance_flat
operator|.
name|Group
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
name|testdo
operator|.
name|inheritance_flat
operator|.
name|Role
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
name|testdo
operator|.
name|inheritance_flat
operator|.
name|User
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
name|testdo
operator|.
name|inheritance_flat
operator|.
name|UserProperties
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
name|testdo
operator|.
name|testmap
operator|.
name|annotations
operator|.
name|Tag2
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|ServerCase
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|UseServerRuntime
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|INHERTITANCE_SINGLE_TABLE1_PROJECT
argument_list|)
specifier|public
class|class
name|LifecycleCallbackRegistry_InheritanceTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ObjectContext
name|context
decl_stmt|;
specifier|public
name|void
name|testAddListener_PostAdd_EntityInheritance
parameter_list|()
block|{
name|LifecycleCallbackRegistry
name|registry
init|=
operator|new
name|LifecycleCallbackRegistry
argument_list|(
name|context
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|setCallbackRegistry
argument_list|(
name|registry
argument_list|)
expr_stmt|;
name|PostAddListenerInherited
name|listener
init|=
operator|new
name|PostAddListenerInherited
argument_list|()
decl_stmt|;
name|registry
operator|.
name|addListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|registry
operator|.
name|getHandler
argument_list|(
name|LifecycleEvent
operator|.
name|POST_ADD
argument_list|)
operator|.
name|listenersSize
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|newObject
argument_list|(
name|User
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a:User;"
argument_list|,
name|listener
operator|.
name|getAndReset
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|newObject
argument_list|(
name|Group
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a:Group;"
argument_list|,
name|listener
operator|.
name|getAndReset
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|newObject
argument_list|(
name|Role
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|listener
operator|.
name|getAndReset
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|newObject
argument_list|(
name|UserProperties
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|listener
operator|.
name|getAndReset
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_class
class|class
name|PostAddListenerInherited
block|{
name|StringBuilder
name|callbackBuffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
annotation|@
name|PostAdd
argument_list|(
name|entityAnnotations
operator|=
name|Tag2
operator|.
name|class
argument_list|)
name|void
name|postAddAnnotated
parameter_list|(
name|Persistent
name|object
parameter_list|)
block|{
name|callbackBuffer
operator|.
name|append
argument_list|(
literal|"a:"
operator|+
name|object
operator|.
name|getObjectId
argument_list|()
operator|.
name|getEntityName
argument_list|()
operator|+
literal|";"
argument_list|)
expr_stmt|;
block|}
name|String
name|getAndReset
parameter_list|()
block|{
name|String
name|v
init|=
name|callbackBuffer
operator|.
name|toString
argument_list|()
decl_stmt|;
name|callbackBuffer
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
return|return
name|v
return|;
block|}
block|}
end_class

end_unit
