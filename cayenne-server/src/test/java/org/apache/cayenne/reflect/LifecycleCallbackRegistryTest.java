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
name|LifecycleListener
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
name|annotation
operator|.
name|PostLoad
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
name|PostPersist
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
name|PostRemove
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
name|PostUpdate
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
name|PrePersist
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
name|PreRemove
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
name|PreUpdate
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
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|*
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|LifecycleCallbackRegistryTest
block|{
name|LifecycleCallbackRegistry
name|registry
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|createRegistry
parameter_list|()
block|{
name|EntityResolver
name|entityResolver
init|=
name|mock
argument_list|(
name|EntityResolver
operator|.
name|class
argument_list|)
decl_stmt|;
name|registry
operator|=
operator|new
name|LifecycleCallbackRegistry
argument_list|(
name|entityResolver
argument_list|)
expr_stmt|;
for|for
control|(
name|LifecycleEvent
name|event
range|:
name|LifecycleEvent
operator|.
name|values
argument_list|()
control|)
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|registry
operator|.
name|getHandler
argument_list|(
name|event
argument_list|)
operator|.
name|defaultListenersSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|registry
operator|.
name|getHandler
argument_list|(
name|event
argument_list|)
operator|.
name|listenersSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|addDefaultListener
parameter_list|()
throws|throws
name|Exception
block|{
name|LifecycleListener
name|listener
init|=
name|mock
argument_list|(
name|LifecycleListener
operator|.
name|class
argument_list|)
decl_stmt|;
name|registry
operator|.
name|addDefaultListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
for|for
control|(
name|LifecycleEvent
name|event
range|:
name|LifecycleEvent
operator|.
name|values
argument_list|()
control|)
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|registry
operator|.
name|getHandler
argument_list|(
name|event
argument_list|)
operator|.
name|defaultListenersSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|registry
operator|.
name|getHandler
argument_list|(
name|event
argument_list|)
operator|.
name|listenersSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|addDefaultListenerSingleType
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|LifecycleEvent
name|event
range|:
name|LifecycleEvent
operator|.
name|values
argument_list|()
control|)
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|registry
operator|.
name|getHandler
argument_list|(
name|event
argument_list|)
operator|.
name|defaultListenersSize
argument_list|()
argument_list|)
expr_stmt|;
name|LifecycleListener
name|listener
init|=
name|mock
argument_list|(
name|LifecycleListener
operator|.
name|class
argument_list|)
decl_stmt|;
name|registry
operator|.
name|addDefaultListener
argument_list|(
name|event
argument_list|,
name|listener
argument_list|,
name|nameToCamelCase
argument_list|(
name|event
operator|.
name|name
argument_list|()
argument_list|)
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
name|event
argument_list|)
operator|.
name|defaultListenersSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|registry
operator|.
name|getHandler
argument_list|(
name|event
argument_list|)
operator|.
name|listenersSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|addListenerWithEntityClass
parameter_list|()
throws|throws
name|Exception
block|{
name|LifecycleListener
name|listener
init|=
name|mock
argument_list|(
name|LifecycleListener
operator|.
name|class
argument_list|)
decl_stmt|;
name|registry
operator|.
name|addListener
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|listener
argument_list|)
expr_stmt|;
for|for
control|(
name|LifecycleEvent
name|event
range|:
name|LifecycleEvent
operator|.
name|values
argument_list|()
control|)
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|registry
operator|.
name|getHandler
argument_list|(
name|event
argument_list|)
operator|.
name|listenersSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|registry
operator|.
name|getHandler
argument_list|(
name|event
argument_list|)
operator|.
name|defaultListenersSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|addListenerWithEntityClassSingleType
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|LifecycleEvent
name|event
range|:
name|LifecycleEvent
operator|.
name|values
argument_list|()
control|)
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|registry
operator|.
name|getHandler
argument_list|(
name|event
argument_list|)
operator|.
name|listenersSize
argument_list|()
argument_list|)
expr_stmt|;
name|LifecycleListener
name|listener
init|=
name|mock
argument_list|(
name|LifecycleListener
operator|.
name|class
argument_list|)
decl_stmt|;
name|registry
operator|.
name|addListener
argument_list|(
name|event
argument_list|,
name|Object
operator|.
name|class
argument_list|,
name|listener
argument_list|,
name|nameToCamelCase
argument_list|(
name|event
operator|.
name|name
argument_list|()
argument_list|)
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
name|event
argument_list|)
operator|.
name|listenersSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|registry
operator|.
name|getHandler
argument_list|(
name|event
argument_list|)
operator|.
name|defaultListenersSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|addAnnotatedListener
parameter_list|()
block|{
name|registry
operator|.
name|addListener
argument_list|(
operator|new
name|AnnotatedListener
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|LifecycleEvent
name|event
range|:
name|LifecycleEvent
operator|.
name|values
argument_list|()
control|)
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|registry
operator|.
name|getHandler
argument_list|(
name|event
argument_list|)
operator|.
name|defaultListenersSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|registry
operator|.
name|getHandler
argument_list|(
name|event
argument_list|)
operator|.
name|listenersSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|addAnnotatedListenerWithEntityClass
parameter_list|()
block|{
name|registry
operator|.
name|addListener
argument_list|(
operator|new
name|AnnotatedListenerWithEntity
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|LifecycleEvent
name|event
range|:
name|LifecycleEvent
operator|.
name|values
argument_list|()
control|)
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|registry
operator|.
name|getHandler
argument_list|(
name|event
argument_list|)
operator|.
name|defaultListenersSize
argument_list|()
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
name|event
argument_list|)
operator|.
name|listenersSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
class|class
name|AnnotatedListener
block|{
annotation|@
name|PostAdd
specifier|public
name|void
name|postAdd
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
block|}
annotation|@
name|PostLoad
specifier|public
name|void
name|postLoad
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
block|}
annotation|@
name|PostPersist
specifier|public
name|void
name|postPersist
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
block|}
annotation|@
name|PostRemove
specifier|public
name|void
name|postRemove
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
block|}
annotation|@
name|PostUpdate
specifier|public
name|void
name|postUpdate
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
block|}
annotation|@
name|PrePersist
specifier|public
name|void
name|prePersist
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
block|}
annotation|@
name|PreRemove
specifier|public
name|void
name|preRemove
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
block|}
annotation|@
name|PreUpdate
specifier|public
name|void
name|preUpdate
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
block|}
block|}
specifier|private
specifier|static
class|class
name|AnnotatedListenerWithEntity
block|{
annotation|@
name|PostAdd
argument_list|(
name|Object
operator|.
name|class
argument_list|)
specifier|public
name|void
name|postAdd
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
block|}
annotation|@
name|PostLoad
argument_list|(
name|Object
operator|.
name|class
argument_list|)
specifier|public
name|void
name|postLoad
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
block|}
annotation|@
name|PostPersist
argument_list|(
name|Object
operator|.
name|class
argument_list|)
specifier|public
name|void
name|postPersist
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
block|}
annotation|@
name|PostRemove
argument_list|(
name|Object
operator|.
name|class
argument_list|)
specifier|public
name|void
name|postRemove
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
block|}
annotation|@
name|PostUpdate
argument_list|(
name|Object
operator|.
name|class
argument_list|)
specifier|public
name|void
name|postUpdate
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
block|}
annotation|@
name|PrePersist
argument_list|(
name|Object
operator|.
name|class
argument_list|)
specifier|public
name|void
name|prePersist
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
block|}
annotation|@
name|PreRemove
argument_list|(
name|Object
operator|.
name|class
argument_list|)
specifier|public
name|void
name|preRemove
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
block|}
annotation|@
name|PreUpdate
argument_list|(
name|Object
operator|.
name|class
argument_list|)
specifier|public
name|void
name|preUpdate
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
block|}
block|}
specifier|private
specifier|static
name|String
name|nameToCamelCase
parameter_list|(
name|String
name|functionName
parameter_list|)
block|{
name|String
index|[]
name|parts
init|=
name|functionName
operator|.
name|split
argument_list|(
literal|"_"
argument_list|)
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|String
name|part
range|:
name|parts
control|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|part
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|char
index|[]
name|chars
init|=
name|part
operator|.
name|toLowerCase
argument_list|()
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
name|chars
index|[
literal|0
index|]
operator|=
name|Character
operator|.
name|toTitleCase
argument_list|(
name|chars
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|chars
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

