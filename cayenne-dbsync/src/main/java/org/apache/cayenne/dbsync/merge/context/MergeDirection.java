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
name|context
package|;
end_package

begin_comment
comment|/**  * Represent a merge direction that can be either from the model to the db or from the db to the model.  */
end_comment

begin_enum
specifier|public
enum|enum
name|MergeDirection
block|{
comment|/**      * TO_DB Token means that changes was made in object model and should be reflected at DB      */
name|TO_DB
argument_list|(
literal|"To DB"
argument_list|)
block|,
comment|/**      * TO_MODEL Token represent database changes that should be allayed to object model      */
name|TO_MODEL
argument_list|(
literal|"To Model"
argument_list|)
block|;
specifier|private
name|String
name|name
decl_stmt|;
name|MergeDirection
parameter_list|(
name|String
name|name
parameter_list|)
block|{
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
name|isToDb
parameter_list|()
block|{
return|return
operator|(
name|this
operator|==
name|TO_DB
operator|)
return|;
block|}
specifier|public
name|boolean
name|isToModel
parameter_list|()
block|{
return|return
operator|(
name|this
operator|==
name|TO_MODEL
operator|)
return|;
block|}
annotation|@
name|Override
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
name|this
condition|)
block|{
case|case
name|TO_DB
case|:
return|return
name|TO_MODEL
return|;
case|case
name|TO_MODEL
case|:
return|return
name|TO_DB
return|;
default|default:
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Invalid direction: "
operator|+
name|this
argument_list|)
throw|;
block|}
block|}
block|}
end_enum

end_unit

