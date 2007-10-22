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
name|merge
package|;
end_package

begin_comment
comment|/**  * Represent a merge direction that can be one of two. Either from the model to the db or  * from the db to the model.  *   * @author halset  */
end_comment

begin_class
specifier|public
class|class
name|MergeDirection
block|{
specifier|public
specifier|static
specifier|final
name|int
name|TO_DB_ID
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|TO_MODEL_ID
init|=
literal|2
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|MergeDirection
name|TO_DB
init|=
operator|new
name|MergeDirection
argument_list|(
name|TO_DB_ID
argument_list|,
literal|"To DB"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|MergeDirection
name|TO_MODEL
init|=
operator|new
name|MergeDirection
argument_list|(
name|TO_MODEL_ID
argument_list|,
literal|"To Model"
argument_list|)
decl_stmt|;
specifier|private
name|int
name|id
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|MergeDirection
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
return|return
operator|(
name|obj
operator|==
name|this
operator|)
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|id
operator|*
literal|17
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getName
argument_list|()
return|;
block|}
specifier|public
name|MergeDirection
name|reverseDirection
parameter_list|()
block|{
switch|switch
condition|(
name|id
condition|)
block|{
case|case
name|TO_DB_ID
case|:
return|return
name|TO_MODEL
return|;
case|case
name|TO_MODEL_ID
case|:
return|return
name|TO_DB
return|;
default|default:
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Invalid direction id: "
operator|+
name|id
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

