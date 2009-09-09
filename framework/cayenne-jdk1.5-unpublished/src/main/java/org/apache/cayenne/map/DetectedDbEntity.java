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
name|map
package|;
end_package

begin_comment
comment|/**  * A {@link DbEntity} subclass used to hold extra JDBC metadata.  */
end_comment

begin_class
specifier|public
class|class
name|DetectedDbEntity
extends|extends
name|DbEntity
block|{
specifier|protected
name|String
name|primaryKeyName
decl_stmt|;
specifier|public
name|DetectedDbEntity
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the optional primary key name of this DbEntity. This is not the same as the      * name of the DbAttribute, but the name of the unique constraint.      */
specifier|public
name|void
name|setPrimaryKeyName
parameter_list|(
name|String
name|primaryKeyName
parameter_list|)
block|{
name|this
operator|.
name|primaryKeyName
operator|=
name|primaryKeyName
expr_stmt|;
block|}
comment|/**      * Returns the optional primary key name of this DbEntity. This is not the same as the      * name of the DbAttribute, but the name of the unique constraint.      */
specifier|public
name|String
name|getPrimaryKeyName
parameter_list|()
block|{
return|return
name|primaryKeyName
return|;
block|}
block|}
end_class

end_unit

