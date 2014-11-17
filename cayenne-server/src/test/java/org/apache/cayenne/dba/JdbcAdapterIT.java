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
name|dba
operator|.
name|mysql
operator|.
name|MySQLAdapter
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
name|DataMap
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
name|map
operator|.
name|DbKeyGenerator
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
name|CayenneProjects
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|JdbcAdapterIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DbAdapter
name|dbAdapter
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|AdhocObjectFactory
name|objectFactory
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testExternalTypesForJdbcType
parameter_list|()
throws|throws
name|Exception
block|{
comment|// check a few types
name|checkType
argument_list|(
name|Types
operator|.
name|BLOB
argument_list|)
expr_stmt|;
name|checkType
argument_list|(
name|Types
operator|.
name|ARRAY
argument_list|)
expr_stmt|;
name|checkType
argument_list|(
name|Types
operator|.
name|DATE
argument_list|)
expr_stmt|;
name|checkType
argument_list|(
name|Types
operator|.
name|VARCHAR
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkType
parameter_list|(
name|int
name|type
parameter_list|)
throws|throws
name|java
operator|.
name|lang
operator|.
name|Exception
block|{
name|JdbcAdapter
name|adapter
init|=
name|objectFactory
operator|.
name|newInstance
argument_list|(
name|JdbcAdapter
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
index|[]
name|types
init|=
name|adapter
operator|.
name|externalTypesForJdbcType
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|types
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|types
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TypesMapping
operator|.
name|getSqlNameByType
argument_list|(
name|type
argument_list|)
argument_list|,
name|types
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateTableQuoteSqlIdentifiers
parameter_list|()
block|{
if|if
condition|(
name|dbAdapter
operator|instanceof
name|MySQLAdapter
condition|)
block|{
name|DbEntity
name|entity
init|=
operator|new
name|DbEntity
argument_list|()
decl_stmt|;
name|DbAttribute
name|attr
init|=
operator|new
name|DbAttribute
argument_list|()
decl_stmt|;
name|attr
operator|.
name|setName
argument_list|(
literal|"name column"
argument_list|)
expr_stmt|;
name|attr
operator|.
name|setType
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|entity
operator|.
name|addAttribute
argument_list|(
name|attr
argument_list|)
expr_stmt|;
name|DbKeyGenerator
name|id
init|=
operator|new
name|DbKeyGenerator
argument_list|()
decl_stmt|;
name|entity
operator|.
name|setPrimaryKeyGenerator
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|DataMap
name|dm
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|dm
operator|.
name|setQuotingSQLIdentifiers
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|entity
operator|.
name|setDataMap
argument_list|(
name|dm
argument_list|)
expr_stmt|;
name|entity
operator|.
name|setName
argument_list|(
literal|"name table"
argument_list|)
expr_stmt|;
name|MySQLAdapter
name|adaptMySQL
init|=
operator|(
name|MySQLAdapter
operator|)
name|dbAdapter
decl_stmt|;
name|String
name|str
init|=
literal|"CREATE TABLE `name table` (`name column` CHAR NULL) ENGINE=InnoDB"
decl_stmt|;
name|assertEquals
argument_list|(
name|str
argument_list|,
name|adaptMySQL
operator|.
name|createTable
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

