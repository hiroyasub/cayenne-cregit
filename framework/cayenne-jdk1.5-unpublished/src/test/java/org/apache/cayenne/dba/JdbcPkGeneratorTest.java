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
name|java
operator|.
name|util
operator|.
name|Collections
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
name|DataNode
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
name|testdo
operator|.
name|testmap
operator|.
name|Artist
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
name|JdbcPkGeneratorTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DbAdapter
name|adapter
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DataNode
name|node
decl_stmt|;
specifier|public
name|void
name|testLongPk
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|JdbcPkGenerator
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|adapter
operator|.
name|getPkGenerator
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
name|DbEntity
name|artistEntity
init|=
name|node
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupObjEntity
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
name|DbAttribute
name|pkAttribute
init|=
operator|(
name|DbAttribute
operator|)
name|artistEntity
operator|.
name|getAttribute
argument_list|(
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|)
decl_stmt|;
name|JdbcPkGenerator
name|pkGenerator
init|=
operator|(
name|JdbcPkGenerator
operator|)
name|adapter
operator|.
name|getPkGenerator
argument_list|()
decl_stmt|;
name|pkGenerator
operator|.
name|setPkStartValue
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
operator|*
literal|2l
argument_list|)
expr_stmt|;
name|pkGenerator
operator|.
name|createAutoPk
argument_list|(
name|node
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|artistEntity
argument_list|)
argument_list|)
expr_stmt|;
name|pkGenerator
operator|.
name|reset
argument_list|()
expr_stmt|;
name|Object
name|pk
init|=
name|pkGenerator
operator|.
name|generatePk
argument_list|(
name|node
argument_list|,
name|pkAttribute
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|pk
operator|instanceof
name|Long
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"PK is too small: "
operator|+
name|pk
argument_list|,
operator|(
operator|(
name|Long
operator|)
name|pk
operator|)
operator|.
name|longValue
argument_list|()
operator|>
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

