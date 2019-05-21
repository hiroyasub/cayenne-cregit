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
name|dbsync
operator|.
name|merge
operator|.
name|builders
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
name|DbEntity
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
name|DbJoin
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
name|DbRelationship
import|;
end_import

begin_comment
comment|/**  * @since 4.0.  */
end_comment

begin_class
specifier|public
class|class
name|DbRelationshipBuilder
extends|extends
name|DefaultBuilder
argument_list|<
name|DbRelationship
argument_list|>
block|{
specifier|private
name|String
index|[]
name|from
decl_stmt|;
specifier|private
name|String
index|[]
name|to
decl_stmt|;
specifier|public
name|DbRelationshipBuilder
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|DbRelationship
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DbRelationshipBuilder
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|DbRelationship
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DbRelationshipBuilder
parameter_list|(
name|DbRelationship
name|obj
parameter_list|)
block|{
name|super
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DbRelationshipBuilder
name|name
parameter_list|()
block|{
return|return
name|name
argument_list|(
name|getRandomJavaName
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|DbRelationshipBuilder
name|name
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|obj
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|DbRelationshipBuilder
name|from
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|String
modifier|...
name|columns
parameter_list|)
block|{
name|obj
operator|.
name|setSourceEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|this
operator|.
name|from
operator|=
name|columns
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|DbRelationshipBuilder
name|to
parameter_list|(
name|String
name|entityName
parameter_list|,
name|String
modifier|...
name|columns
parameter_list|)
block|{
name|obj
operator|.
name|setTargetEntityName
argument_list|(
name|entityName
argument_list|)
expr_stmt|;
name|this
operator|.
name|to
operator|=
name|columns
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|DbRelationship
name|build
parameter_list|()
block|{
if|if
condition|(
name|obj
operator|.
name|getName
argument_list|()
operator|==
literal|null
condition|)
block|{
name|name
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|from
operator|.
name|length
operator|!=
name|to
operator|.
name|length
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"from and to columns name size mismatch"
argument_list|)
throw|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|from
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|obj
operator|.
name|addJoin
argument_list|(
operator|new
name|DbJoin
argument_list|(
name|obj
argument_list|,
name|from
index|[
name|i
index|]
argument_list|,
name|to
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|obj
return|;
block|}
block|}
end_class

end_unit

