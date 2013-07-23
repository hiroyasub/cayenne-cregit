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
operator|.
name|naming
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_comment
comment|/**  * ExportedKey is an representation of relationship between two tables   * in database. It can be used for creating names for relationships  *   */
end_comment

begin_class
specifier|public
class|class
name|ExportedKey
block|{
comment|/**      * Name of source table      */
name|String
name|pkTable
decl_stmt|;
comment|/**      * Name of source column      */
name|String
name|pkColumn
decl_stmt|;
comment|/**      * Name of destination table      */
name|String
name|fkTable
decl_stmt|;
comment|/**      * Name of destination column      */
name|String
name|fkColumn
decl_stmt|;
comment|/**      * Name of foreign key (might be null)      */
name|String
name|fkName
decl_stmt|;
comment|/**      * Name of primary key (might be null)      */
name|String
name|pkName
decl_stmt|;
specifier|public
name|ExportedKey
parameter_list|(
name|String
name|pkTable
parameter_list|,
name|String
name|pkColumn
parameter_list|,
name|String
name|pkName
parameter_list|,
name|String
name|fkTable
parameter_list|,
name|String
name|fkColumn
parameter_list|,
name|String
name|fkName
parameter_list|)
block|{
name|this
operator|.
name|pkTable
operator|=
name|pkTable
expr_stmt|;
name|this
operator|.
name|pkColumn
operator|=
name|pkColumn
expr_stmt|;
name|this
operator|.
name|pkName
operator|=
name|pkName
expr_stmt|;
name|this
operator|.
name|fkTable
operator|=
name|fkTable
expr_stmt|;
name|this
operator|.
name|fkColumn
operator|=
name|fkColumn
expr_stmt|;
name|this
operator|.
name|fkName
operator|=
name|fkName
expr_stmt|;
block|}
comment|/**      * Extracts data from a resultset pointing to a exported key to      * ExportedKey class instance      *       * @param rs ResultSet pointing to a exported key, fetched using      * DataBaseMetaData.getExportedKeys(...)       */
specifier|public
specifier|static
name|ExportedKey
name|extractData
parameter_list|(
name|ResultSet
name|rs
parameter_list|)
throws|throws
name|SQLException
block|{
name|ExportedKey
name|key
init|=
operator|new
name|ExportedKey
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"PKTABLE_NAME"
argument_list|)
argument_list|,
name|rs
operator|.
name|getString
argument_list|(
literal|"PKCOLUMN_NAME"
argument_list|)
argument_list|,
name|rs
operator|.
name|getString
argument_list|(
literal|"PK_NAME"
argument_list|)
argument_list|,
name|rs
operator|.
name|getString
argument_list|(
literal|"FKTABLE_NAME"
argument_list|)
argument_list|,
name|rs
operator|.
name|getString
argument_list|(
literal|"FKCOLUMN_NAME"
argument_list|)
argument_list|,
name|rs
operator|.
name|getString
argument_list|(
literal|"FK_NAME"
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|key
return|;
block|}
comment|/**      * @return source table name      */
specifier|public
name|String
name|getPKTableName
parameter_list|()
block|{
return|return
name|pkTable
return|;
block|}
comment|/**      * @return destination table name      */
specifier|public
name|String
name|getFKTableName
parameter_list|()
block|{
return|return
name|fkTable
return|;
block|}
comment|/**      * @return source column name      */
specifier|public
name|String
name|getPKColumnName
parameter_list|()
block|{
return|return
name|pkColumn
return|;
block|}
comment|/**      * @return destination column name      */
specifier|public
name|String
name|getFKColumnName
parameter_list|()
block|{
return|return
name|fkColumn
return|;
block|}
comment|/**      * @return PK name      */
specifier|public
name|String
name|getPKName
parameter_list|()
block|{
return|return
name|pkName
return|;
block|}
comment|/**      * @return FK name      */
specifier|public
name|String
name|getFKName
parameter_list|()
block|{
return|return
name|fkName
return|;
block|}
block|}
end_class

end_unit
