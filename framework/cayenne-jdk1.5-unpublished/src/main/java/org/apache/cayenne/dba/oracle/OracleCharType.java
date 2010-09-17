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
name|oracle
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|PreparedStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Types
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
name|access
operator|.
name|types
operator|.
name|CharType
import|;
end_import

begin_comment
comment|/**  * Oracle specific CHAR type handling.   *   * If connection property "fixedString" is true, then  * 'a' equal 'a ' in a CHAR(4) while parameter binding in PreparedStatement.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|OracleCharType
extends|extends
name|CharType
block|{
specifier|public
name|OracleCharType
parameter_list|()
block|{
name|super
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setJdbcObject
parameter_list|(
name|PreparedStatement
name|st
parameter_list|,
name|Object
name|val
parameter_list|,
name|int
name|pos
parameter_list|,
name|int
name|type
parameter_list|,
name|int
name|precision
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|type
operator|==
name|Types
operator|.
name|CLOB
condition|)
block|{
name|st
operator|.
name|setString
argument_list|(
name|pos
argument_list|,
operator|(
name|String
operator|)
name|val
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// use exactly this method to solve CAY-1470
comment|// setting connection property "fixedString" to true doesn't help
comment|// with .setString(parameterIndex, x)
comment|// nether with .setObject(parameterIndex, x, targetSqlType)
name|st
operator|.
name|setObject
argument_list|(
name|pos
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

