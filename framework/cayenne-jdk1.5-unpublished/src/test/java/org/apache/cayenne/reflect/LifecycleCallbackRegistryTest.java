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
name|testdo
operator|.
name|testmap
operator|.
name|Artist
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
name|Exhibit
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
name|Gallery
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
name|Painting
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
name|Tag1
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
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|LifecycleCallbackRegistryTest
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
name|testAddListener_PostAdd
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
name|PostAddListener
name|listener
init|=
operator|new
name|PostAddListener
argument_list|()
decl_stmt|;
name|registry
operator|.
name|addListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|context
operator|.
name|newObject
argument_list|(
name|Gallery
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"e:Gallery;"
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
name|Artist
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a:Artist;"
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
name|Exhibit
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
name|Painting
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"e:Painting;"
argument_list|,
name|listener
operator|.
name|getAndReset
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddListener_PostAdd_InheritedListenerMethods
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
name|PostAddListenerSubclass
name|listener
init|=
operator|new
name|PostAddListenerSubclass
argument_list|()
decl_stmt|;
name|registry
operator|.
name|addListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|context
operator|.
name|newObject
argument_list|(
name|Gallery
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"e:Gallery;"
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
name|Artist
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a:Artist;"
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
name|Exhibit
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
name|Painting
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"e:Painting;"
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
name|PostAddListener
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
block|{
name|Gallery
operator|.
name|class
block|,
name|Painting
operator|.
name|class
block|}
argument_list|)
name|void
name|postAddEntities
parameter_list|(
name|Persistent
name|object
parameter_list|)
block|{
name|callbackBuffer
operator|.
name|append
argument_list|(
literal|"e:"
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
annotation|@
name|PostAdd
argument_list|(
name|entityAnnotations
operator|=
name|Tag1
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

begin_class
class|class
name|PostAddListenerSubclass
extends|extends
name|PostAddListener
block|{  }
end_class

end_unit

