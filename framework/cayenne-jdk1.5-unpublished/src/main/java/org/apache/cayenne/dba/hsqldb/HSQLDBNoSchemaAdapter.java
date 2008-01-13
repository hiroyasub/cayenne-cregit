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
name|dba
operator|.
name|hsqldb
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

begin_comment
comment|/**  * A flavor of HSQLDBAdapter that implements workarounds for some old driver limitations.  *   * @since 1.2  * @author Mike Kienenberger  */
end_comment

begin_class
specifier|public
class|class
name|HSQLDBNoSchemaAdapter
extends|extends
name|HSQLDBAdapter
block|{
comment|/**      * Generate unqualified name without schema.      *       * @since 1.2      */
annotation|@
name|Override
specifier|protected
name|String
name|getTableName
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
return|return
name|entity
operator|.
name|getName
argument_list|()
return|;
block|}
comment|/**      * Generate unqualified name.      *       * @since 1.2      */
annotation|@
name|Override
specifier|protected
name|String
name|getSchemaName
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
return|return
literal|""
return|;
block|}
comment|/**      * Returns a SQL string to drop a table corresponding to<code>ent</code> DbEntity.      *       * @since 1.2      */
annotation|@
name|Override
specifier|public
name|String
name|dropTable
parameter_list|(
name|DbEntity
name|ent
parameter_list|)
block|{
comment|// hsqldb doesn't support schema namespaces, so remove if found
return|return
literal|"DROP TABLE "
operator|+
name|getTableName
argument_list|(
name|ent
argument_list|)
return|;
block|}
comment|/**      * Uses unqualified entity names.      *       * @since 1.2      */
annotation|@
name|Override
specifier|public
name|String
name|createTable
parameter_list|(
name|DbEntity
name|ent
parameter_list|)
block|{
name|String
name|sql
init|=
name|super
operator|.
name|createTable
argument_list|(
name|ent
argument_list|)
decl_stmt|;
comment|// hsqldb doesn't support schema namespaces, so remove if found
name|String
name|fqnCreate
init|=
literal|"CREATE CACHED TABLE "
operator|+
name|super
operator|.
name|getTableName
argument_list|(
name|ent
argument_list|)
operator|+
literal|" ("
decl_stmt|;
if|if
condition|(
name|sql
operator|!=
literal|null
operator|&&
name|sql
operator|.
name|toUpperCase
argument_list|()
operator|.
name|startsWith
argument_list|(
name|fqnCreate
argument_list|)
condition|)
block|{
name|sql
operator|=
literal|"CREATE CACHED TABLE "
operator|+
name|getTableName
argument_list|(
name|ent
argument_list|)
operator|+
literal|" ("
operator|+
name|sql
operator|.
name|substring
argument_list|(
name|fqnCreate
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|sql
return|;
block|}
block|}
end_class

end_unit

