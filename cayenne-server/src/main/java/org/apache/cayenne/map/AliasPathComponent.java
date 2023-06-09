begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_comment
comment|/**  * Represents an alias for the relationship path.  *   * @since 3.0  */
end_comment

begin_class
class|class
name|AliasPathComponent
parameter_list|<
name|A
extends|extends
name|Attribute
parameter_list|<
name|?
parameter_list|,
name|A
parameter_list|,
name|R
parameter_list|>
parameter_list|,
name|R
extends|extends
name|Relationship
parameter_list|<
name|?
parameter_list|,
name|A
parameter_list|,
name|R
parameter_list|>
parameter_list|>
implements|implements
name|PathComponent
argument_list|<
name|A
argument_list|,
name|R
argument_list|>
block|{
specifier|private
specifier|final
name|String
name|alias
decl_stmt|;
specifier|private
specifier|final
name|Collection
argument_list|<
name|PathComponent
argument_list|<
name|A
argument_list|,
name|R
argument_list|>
argument_list|>
name|path
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|last
decl_stmt|;
name|AliasPathComponent
parameter_list|(
name|String
name|alias
parameter_list|,
name|Collection
argument_list|<
name|PathComponent
argument_list|<
name|A
argument_list|,
name|R
argument_list|>
argument_list|>
name|path
parameter_list|,
name|boolean
name|last
parameter_list|)
block|{
name|this
operator|.
name|alias
operator|=
name|alias
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|this
operator|.
name|last
operator|=
name|last
expr_stmt|;
block|}
specifier|public
name|Iterable
argument_list|<
name|PathComponent
argument_list|<
name|A
argument_list|,
name|R
argument_list|>
argument_list|>
name|getAliasedPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
specifier|public
name|A
name|getAttribute
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|JoinType
name|getJoinType
parameter_list|()
block|{
return|return
name|JoinType
operator|.
name|UNDEFINED
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|alias
return|;
block|}
specifier|public
name|R
name|getRelationship
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isAlias
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|isLast
parameter_list|()
block|{
return|return
name|last
return|;
block|}
block|}
end_class

end_unit

