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
operator|.
name|generic
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
name|ObjEntity
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
name|reflect
operator|.
name|ArcProperty
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
name|reflect
operator|.
name|AttributeProperty
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
name|reflect
operator|.
name|ClassDescriptor
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
name|reflect
operator|.
name|PropertyDescriptor
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
name|reflect
operator|.
name|PropertyVisitor
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
name|reflect
operator|.
name|SingletonFaultFactory
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
name|reflect
operator|.
name|ToManyProperty
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
name|reflect
operator|.
name|ToOneProperty
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
name|assertTrue
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
name|fail
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
name|DataObjectDescriptorFactoryIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|EntityResolver
name|resolver
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testVisitDeclaredProperties_IterationOrder
parameter_list|()
block|{
name|DataObjectDescriptorFactory
name|factory
init|=
operator|new
name|DataObjectDescriptorFactory
argument_list|(
name|resolver
operator|.
name|getClassDescriptorMap
argument_list|()
argument_list|,
operator|new
name|SingletonFaultFactory
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ObjEntity
name|e
range|:
name|resolver
operator|.
name|getObjEntities
argument_list|()
control|)
block|{
name|ClassDescriptor
name|descriptor
init|=
name|factory
operator|.
name|getDescriptor
argument_list|(
name|e
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|PropertyDescriptor
index|[]
name|lastProcessed
init|=
operator|new
name|PropertyDescriptor
index|[
literal|1
index|]
decl_stmt|;
name|PropertyVisitor
name|visitor
init|=
operator|new
name|PropertyVisitor
argument_list|()
block|{
specifier|public
name|boolean
name|visitToOne
parameter_list|(
name|ToOneProperty
name|property
parameter_list|)
block|{
name|assertPropertiesAreInOrder
argument_list|(
name|lastProcessed
index|[
literal|0
index|]
argument_list|,
name|property
argument_list|)
expr_stmt|;
name|lastProcessed
index|[
literal|0
index|]
operator|=
name|property
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitToMany
parameter_list|(
name|ToManyProperty
name|property
parameter_list|)
block|{
name|assertPropertiesAreInOrder
argument_list|(
name|lastProcessed
index|[
literal|0
index|]
argument_list|,
name|property
argument_list|)
expr_stmt|;
name|lastProcessed
index|[
literal|0
index|]
operator|=
name|property
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitAttribute
parameter_list|(
name|AttributeProperty
name|property
parameter_list|)
block|{
name|assertPropertiesAreInOrder
argument_list|(
name|lastProcessed
index|[
literal|0
index|]
argument_list|,
name|property
argument_list|)
expr_stmt|;
name|lastProcessed
index|[
literal|0
index|]
operator|=
name|property
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
name|descriptor
operator|.
name|visitDeclaredProperties
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testVisitProperties_IterationOrder
parameter_list|()
block|{
name|DataObjectDescriptorFactory
name|factory
init|=
operator|new
name|DataObjectDescriptorFactory
argument_list|(
name|resolver
operator|.
name|getClassDescriptorMap
argument_list|()
argument_list|,
operator|new
name|SingletonFaultFactory
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ObjEntity
name|e
range|:
name|resolver
operator|.
name|getObjEntities
argument_list|()
control|)
block|{
name|ClassDescriptor
name|descriptor
init|=
name|factory
operator|.
name|getDescriptor
argument_list|(
name|e
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|PropertyDescriptor
index|[]
name|lastProcessed
init|=
operator|new
name|PropertyDescriptor
index|[
literal|1
index|]
decl_stmt|;
name|PropertyVisitor
name|visitor
init|=
operator|new
name|PropertyVisitor
argument_list|()
block|{
specifier|public
name|boolean
name|visitToOne
parameter_list|(
name|ToOneProperty
name|property
parameter_list|)
block|{
name|assertPropertiesAreInOrder
argument_list|(
name|lastProcessed
index|[
literal|0
index|]
argument_list|,
name|property
argument_list|)
expr_stmt|;
name|lastProcessed
index|[
literal|0
index|]
operator|=
name|property
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitToMany
parameter_list|(
name|ToManyProperty
name|property
parameter_list|)
block|{
name|assertPropertiesAreInOrder
argument_list|(
name|lastProcessed
index|[
literal|0
index|]
argument_list|,
name|property
argument_list|)
expr_stmt|;
name|lastProcessed
index|[
literal|0
index|]
operator|=
name|property
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitAttribute
parameter_list|(
name|AttributeProperty
name|property
parameter_list|)
block|{
name|assertPropertiesAreInOrder
argument_list|(
name|lastProcessed
index|[
literal|0
index|]
argument_list|,
name|property
argument_list|)
expr_stmt|;
name|lastProcessed
index|[
literal|0
index|]
operator|=
name|property
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
name|descriptor
operator|.
name|visitProperties
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
name|void
name|assertPropertiesAreInOrder
parameter_list|(
name|PropertyDescriptor
name|o1
parameter_list|,
name|PropertyDescriptor
name|o2
parameter_list|)
block|{
if|if
condition|(
name|o1
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|o1
operator|instanceof
name|ArcProperty
condition|)
block|{
if|if
condition|(
name|o2
operator|instanceof
name|ArcProperty
condition|)
block|{
name|assertTrue
argument_list|(
literal|"Names are not ordered: "
operator|+
name|o1
operator|.
name|getName
argument_list|()
operator|+
literal|" before "
operator|+
name|o2
operator|.
name|getName
argument_list|()
argument_list|,
name|o1
operator|.
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getName
argument_list|()
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fail
argument_list|(
literal|"Arc preceded regular property"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|o2
operator|instanceof
name|ArcProperty
condition|)
block|{
comment|// this is correct
block|}
else|else
block|{
name|assertTrue
argument_list|(
literal|"Names are not ordered: "
operator|+
name|o1
operator|.
name|getName
argument_list|()
operator|+
literal|" before "
operator|+
name|o2
operator|.
name|getName
argument_list|()
argument_list|,
name|o1
operator|.
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getName
argument_list|()
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

