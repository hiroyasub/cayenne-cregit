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
name|SQLException
import|;
end_import

begin_comment
comment|/**  * Defines Java stored procedures loaded to HSQLDB.  */
end_comment

begin_class
specifier|public
class|class
name|HSQLProcedures
block|{
specifier|public
specifier|static
name|void
name|cayenne_tst_upd_proc
parameter_list|(
name|Connection
name|c
parameter_list|,
name|int
name|paintingPrice
parameter_list|)
throws|throws
name|SQLException
block|{
try|try
init|(
name|PreparedStatement
name|st
init|=
name|c
operator|.
name|prepareStatement
argument_list|(
literal|"UPDATE PAINTING SET ESTIMATED_PRICE = ESTIMATED_PRICE * 2 "
operator|+
literal|"WHERE ESTIMATED_PRICE< ?"
argument_list|)
init|;
init|)
block|{
name|st
operator|.
name|setInt
argument_list|(
literal|1
argument_list|,
name|paintingPrice
argument_list|)
expr_stmt|;
name|st
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|cayenne_tst_select_proc
parameter_list|(
name|Connection
name|c
parameter_list|,
name|String
name|name
parameter_list|,
name|int
name|paintingPrice
parameter_list|)
throws|throws
name|SQLException
block|{
try|try
init|(
name|PreparedStatement
name|st
init|=
name|c
operator|.
name|prepareStatement
argument_list|(
literal|"UPDATE PAINTING SET ESTIMATED_PRICE = ESTIMATED_PRICE * 2 "
operator|+
literal|"WHERE ESTIMATED_PRICE< ?"
argument_list|)
init|;
init|)
block|{
name|st
operator|.
name|setInt
argument_list|(
literal|1
argument_list|,
name|paintingPrice
argument_list|)
expr_stmt|;
name|st
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
try|try
init|(
name|PreparedStatement
name|select
init|=
name|c
operator|.
name|prepareStatement
argument_list|(
literal|"SELECT DISTINCT A.ARTIST_ID, A.ARTIST_NAME, A.DATE_OF_BIRTH"
operator|+
literal|" FROM ARTIST A, PAINTING P"
operator|+
literal|" WHERE A.ARTIST_ID = P.ARTIST_ID AND"
operator|+
literal|" A.ARTIST_NAME = ?"
operator|+
literal|" ORDER BY A.ARTIST_ID"
argument_list|)
init|;
init|)
block|{
name|select
operator|.
name|setString
argument_list|(
literal|1
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|select
operator|.
name|executeQuery
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

