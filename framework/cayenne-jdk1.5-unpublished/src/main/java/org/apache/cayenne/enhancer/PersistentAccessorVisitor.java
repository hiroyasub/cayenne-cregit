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
name|map
operator|.
name|ObjEntity
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

begin_comment
comment|/**  * Accessor enhancer that enhances getters and setters mapped in a given {@link ObjEntity}.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|PersistentAccessorVisitor
extends|extends
name|AccessorVisitor
block|{
specifier|private
name|ObjEntity
name|entity
decl_stmt|;
specifier|public
name|PersistentAccessorVisitor
parameter_list|(
name|ClassVisitor
name|visitor
parameter_list|,
name|ObjEntity
name|entity
parameter_list|)
block|{
name|super
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
name|this
operator|.
name|entity
operator|=
name|entity
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isEnhancedProperty
parameter_list|(
name|String
name|property
parameter_list|)
block|{
return|return
name|entity
operator|.
name|getAttribute
argument_list|(
name|property
argument_list|)
operator|!=
literal|null
operator|||
name|entity
operator|.
name|getRelationship
argument_list|(
name|property
argument_list|)
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isLazyFaulted
parameter_list|(
name|String
name|property
parameter_list|)
block|{
return|return
name|entity
operator|.
name|getRelationship
argument_list|(
name|property
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit

