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
name|access
operator|.
name|trans
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
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
name|dba
operator|.
name|JdbcAdapter
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
name|di
operator|.
name|AdhocObjectFactory
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
name|di
operator|.
name|Inject
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
name|DbAttribute
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
name|query
operator|.
name|BatchQuery
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|ServerCase
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|UseServerRuntime
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|BatchQueryBuilderTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|AdhocObjectFactory
name|objectFactory
decl_stmt|;
specifier|public
name|void
name|testConstructor
parameter_list|()
throws|throws
name|Exception
block|{
name|DbAdapter
name|adapter
init|=
name|objectFactory
operator|.
name|newInstance
argument_list|(
name|DbAdapter
operator|.
name|class
argument_list|,
name|JdbcAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|BatchQueryBuilder
name|builder
init|=
operator|new
name|BatchQueryBuilder
argument_list|(
name|mock
argument_list|(
name|BatchQuery
operator|.
name|class
argument_list|)
argument_list|,
name|adapter
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|String
name|createSqlString
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|bindParameters
parameter_list|(
name|PreparedStatement
name|statement
parameter_list|)
throws|throws
name|SQLException
throws|,
name|Exception
block|{
block|}
block|}
decl_stmt|;
name|assertSame
argument_list|(
name|adapter
argument_list|,
name|builder
operator|.
name|getAdapter
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAppendDbAttribute1
parameter_list|()
throws|throws
name|Exception
block|{
name|DbAdapter
name|adapter
init|=
name|objectFactory
operator|.
name|newInstance
argument_list|(
name|DbAdapter
operator|.
name|class
argument_list|,
name|JdbcAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|trimFunction
init|=
literal|"testTrim"
decl_stmt|;
name|BatchQueryBuilder
name|builder
init|=
operator|new
name|BatchQueryBuilder
argument_list|(
name|mock
argument_list|(
name|BatchQuery
operator|.
name|class
argument_list|)
argument_list|,
name|adapter
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|String
name|createSqlString
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|bindParameters
parameter_list|(
name|PreparedStatement
name|statement
parameter_list|)
throws|throws
name|SQLException
throws|,
name|Exception
block|{
block|}
block|}
decl_stmt|;
name|builder
operator|.
name|setTrimFunction
argument_list|(
name|trimFunction
argument_list|)
expr_stmt|;
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|DbEntity
name|entity
init|=
operator|new
name|DbEntity
argument_list|(
literal|"Test"
argument_list|)
decl_stmt|;
name|DbAttribute
name|attr
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"testAttr"
argument_list|,
name|Types
operator|.
name|CHAR
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|attr
operator|.
name|setEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|builder
operator|.
name|appendDbAttribute
argument_list|(
name|buf
argument_list|,
name|attr
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"testTrim(testAttr)"
argument_list|,
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
name|attr
operator|=
operator|new
name|DbAttribute
argument_list|(
literal|"testAttr"
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|attr
operator|.
name|setEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|builder
operator|.
name|appendDbAttribute
argument_list|(
name|buf
argument_list|,
name|attr
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"testAttr"
argument_list|,
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAppendDbAttribute2
parameter_list|()
throws|throws
name|Exception
block|{
name|DbAdapter
name|adapter
init|=
name|objectFactory
operator|.
name|newInstance
argument_list|(
name|DbAdapter
operator|.
name|class
argument_list|,
name|JdbcAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|BatchQueryBuilder
name|builder
init|=
operator|new
name|BatchQueryBuilder
argument_list|(
name|mock
argument_list|(
name|BatchQuery
operator|.
name|class
argument_list|)
argument_list|,
name|adapter
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|String
name|createSqlString
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|bindParameters
parameter_list|(
name|PreparedStatement
name|statement
parameter_list|)
throws|throws
name|SQLException
throws|,
name|Exception
block|{
block|}
block|}
decl_stmt|;
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|DbEntity
name|entity
init|=
operator|new
name|DbEntity
argument_list|(
literal|"Test"
argument_list|)
decl_stmt|;
name|DbAttribute
name|attr
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"testAttr"
argument_list|,
name|Types
operator|.
name|CHAR
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|attr
operator|.
name|setEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|builder
operator|.
name|appendDbAttribute
argument_list|(
name|buf
argument_list|,
name|attr
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"testAttr"
argument_list|,
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
name|attr
operator|=
operator|new
name|DbAttribute
argument_list|(
literal|"testAttr"
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|attr
operator|.
name|setEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|builder
operator|.
name|appendDbAttribute
argument_list|(
name|buf
argument_list|,
name|attr
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"testAttr"
argument_list|,
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

