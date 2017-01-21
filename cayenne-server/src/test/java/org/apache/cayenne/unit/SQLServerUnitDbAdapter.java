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
name|unit
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|dba
operator|.
name|DbAdapter
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
name|DataMap
import|;
end_import

begin_comment
comment|/**  * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|SQLServerUnitDbAdapter
extends|extends
name|SybaseUnitDbAdapter
block|{
specifier|public
name|SQLServerUnitDbAdapter
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|)
block|{
name|super
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|handlesNullVsEmptyLOBs
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|willCreateTables
parameter_list|(
name|Connection
name|con
parameter_list|,
name|DataMap
name|map
parameter_list|)
throws|throws
name|Exception
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|willDropTables
parameter_list|(
name|Connection
name|conn
parameter_list|,
name|DataMap
name|map
parameter_list|,
name|Collection
name|tablesToDrop
parameter_list|)
throws|throws
name|Exception
block|{
name|dropConstraints
argument_list|(
name|conn
argument_list|,
name|map
argument_list|,
name|tablesToDrop
argument_list|)
expr_stmt|;
name|dropProcedures
argument_list|(
name|conn
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|supportsLobComparisons
parameter_list|()
block|{
comment|// people are suggesting using LIKE to compare TEXT columns... not sure
comment|// what the right solution might be, but for now we are getting
comment|// "The data types varchar(max) and text are incompatible in the equal to operator. in SQL Server2005 how to solve?"
comment|// http://stackoverflow.com/questions/20180766/the-data-types-varcharmax-and-text-are-incompatible-in-the-equal-to-operator
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|supportsNullBoolean
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|onlyGenericDateType
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|// The code below was used with SQLServer<= 2005 to turn of autogenerated
comment|// keys.
comment|// Modern SQLServer driver supports autogen keys just fine.
comment|// public void unchecked(CayenneTestResources resources) {
comment|// // see if MSSQL driver is used and turn off identity columns in this
comment|// case...
comment|//
comment|// String driver = resources.getConnectionInfo().getJdbcDriver();
comment|// if (driver != null&& driver.startsWith("com.microsoft.") ) {
comment|// ((JdbcAdapter) getAdapter()).setSupportsGeneratedKeys(false);
comment|// }
comment|// }
annotation|@
name|Override
specifier|public
name|boolean
name|supportsExpressionInHaving
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|supportsSelectBooleanExpression
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

