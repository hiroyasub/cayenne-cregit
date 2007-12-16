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
name|jpa
operator|.
name|enhancer
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
name|enhancer
operator|.
name|EmbeddableVisitor
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
name|enhancer
operator|.
name|EnhancerVisitorFactory
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
name|enhancer
operator|.
name|PersistentInterfaceVisitor
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
name|jpa
operator|.
name|map
operator|.
name|JpaEmbeddable
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
name|jpa
operator|.
name|map
operator|.
name|JpaEntity
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
name|jpa
operator|.
name|map
operator|.
name|JpaEntityMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|ClassVisitor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|commons
operator|.
name|SerialVersionUIDAdder
import|;
end_import

begin_comment
comment|/**  * Class enhancer used for JPA.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|JpaEnhancerVisitorFactory
implements|implements
name|EnhancerVisitorFactory
block|{
specifier|private
name|JpaEntityMap
name|entityMap
decl_stmt|;
specifier|public
name|JpaEnhancerVisitorFactory
parameter_list|(
name|JpaEntityMap
name|entityMap
parameter_list|)
block|{
name|this
operator|.
name|entityMap
operator|=
name|entityMap
expr_stmt|;
block|}
specifier|public
name|ClassVisitor
name|createVisitor
parameter_list|(
name|String
name|className
parameter_list|,
name|ClassVisitor
name|out
parameter_list|)
block|{
name|String
name|key
init|=
name|className
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
literal|'.'
argument_list|)
decl_stmt|;
name|JpaEntity
name|entity
init|=
name|entityMap
operator|.
name|entityForClass
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|entity
operator|!=
literal|null
condition|)
block|{
comment|// create enhancer chain
name|PersistentInterfaceVisitor
name|e1
init|=
operator|new
name|PersistentInterfaceVisitor
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|JpaAccessorVisitor
name|e2
init|=
operator|new
name|JpaAccessorVisitor
argument_list|(
name|e1
argument_list|,
name|entity
operator|.
name|getClassDescriptor
argument_list|()
argument_list|)
decl_stmt|;
comment|// this ensures that both enhanced and original classes have compatible
comment|// serialized
comment|// format even if no serialVersionUID is defined by the user
name|SerialVersionUIDAdder
name|e3
init|=
operator|new
name|SerialVersionUIDAdder
argument_list|(
name|e2
argument_list|)
decl_stmt|;
return|return
name|e3
return|;
block|}
name|JpaEmbeddable
name|embeddable
init|=
name|entityMap
operator|.
name|embeddableForClass
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|embeddable
operator|!=
literal|null
condition|)
block|{
comment|// create enhancer chain
name|EmbeddableVisitor
name|e1
init|=
operator|new
name|EmbeddableVisitor
argument_list|(
name|out
argument_list|)
decl_stmt|;
comment|// TODO: andrus 12/16/2007 - setter visitor...
comment|// this ensures that both enhanced and original classes have compatible
comment|// serialized
comment|// format even if no serialVersionUID is defined by the user
name|SerialVersionUIDAdder
name|e2
init|=
operator|new
name|SerialVersionUIDAdder
argument_list|(
name|e1
argument_list|)
decl_stmt|;
return|return
name|e2
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

