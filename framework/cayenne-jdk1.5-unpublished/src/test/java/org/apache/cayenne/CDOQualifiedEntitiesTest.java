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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|query
operator|.
name|SelectQuery
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
name|test
operator|.
name|jdbc
operator|.
name|DBHelper
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
name|test
operator|.
name|jdbc
operator|.
name|TableHelper
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
name|testdo
operator|.
name|qualified
operator|.
name|Qualified1
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
name|testdo
operator|.
name|qualified
operator|.
name|Qualified2
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
name|UnitDbAdapter
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
name|DEFAULT_PROJECT
argument_list|)
specifier|public
class|class
name|CDOQualifiedEntitiesTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ObjectContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|UnitDbAdapter
name|accessStackAdapter
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|private
name|TableHelper
name|tQualified1
decl_stmt|;
specifier|private
name|TableHelper
name|tQualified2
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUpAfterInjection
parameter_list|()
throws|throws
name|Exception
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"QUALIFIED2"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"QUALIFIED1"
argument_list|)
expr_stmt|;
name|tQualified1
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"QUALIFIED1"
argument_list|)
expr_stmt|;
name|tQualified1
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"NAME"
argument_list|,
literal|"DELETED"
argument_list|)
expr_stmt|;
name|tQualified2
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"QUALIFIED2"
argument_list|)
expr_stmt|;
name|tQualified2
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"NAME"
argument_list|,
literal|"DELETED"
argument_list|,
literal|"QUALIFIED1_ID"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createReadToManyDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tQualified1
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"OX1"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|tQualified1
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"OX2"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|tQualified2
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"OY1"
argument_list|,
literal|null
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tQualified2
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"OY2"
argument_list|,
literal|true
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tQualified2
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"OY3"
argument_list|,
literal|null
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|tQualified2
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|"OY4"
argument_list|,
literal|true
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createReadToOneDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tQualified1
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"OX1"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|tQualified1
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"OX2"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|tQualified2
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"OY1"
argument_list|,
literal|null
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testReadToMany
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|accessStackAdapter
operator|.
name|supportsNullBoolean
argument_list|()
condition|)
block|{
name|createReadToManyDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|rootSelect
init|=
operator|new
name|SelectQuery
argument_list|(
name|Qualified1
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Qualified1
argument_list|>
name|roots
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|rootSelect
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|roots
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Qualified1
name|root
init|=
name|roots
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"OX1"
argument_list|,
name|root
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Qualified2
argument_list|>
name|related
init|=
name|root
operator|.
name|getQualified2s
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|related
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Qualified2
name|r
init|=
name|related
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"OY1"
argument_list|,
name|r
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testReadToOne
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|accessStackAdapter
operator|.
name|supportsNullBoolean
argument_list|()
condition|)
block|{
name|createReadToOneDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|rootSelect
init|=
operator|new
name|SelectQuery
argument_list|(
name|Qualified2
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Qualified2
argument_list|>
name|roots
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|rootSelect
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|roots
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Qualified2
name|root
init|=
name|roots
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"OY1"
argument_list|,
name|root
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Qualified1
name|target
init|=
name|root
operator|.
name|getQualified1
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
literal|""
operator|+
name|target
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

