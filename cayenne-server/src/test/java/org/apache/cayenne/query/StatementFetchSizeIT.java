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
name|query
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
name|ObjectContext
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
name|ObjectId
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
name|StatementFetchSizeIT
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
name|Test
specifier|public
name|void
name|test
parameter_list|()
block|{
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|setStatementFetchSize
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|query
operator|.
name|getMetaData
argument_list|(
name|context
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
operator|.
name|getStatementFetchSize
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|SQLTemplate
name|template
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"SELECT ARTIST_ID FROM ARTIST"
argument_list|)
decl_stmt|;
name|template
operator|.
name|setStatementFetchSize
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|template
operator|.
name|getMetaData
argument_list|(
name|context
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
operator|.
name|getStatementFetchSize
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
name|template
argument_list|)
expr_stmt|;
name|EJBQLQuery
name|ejbql
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"select a from Artist a"
argument_list|)
decl_stmt|;
name|ejbql
operator|.
name|setStatementFetchSize
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|ejbql
operator|.
name|getMetaData
argument_list|(
name|context
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
operator|.
name|getStatementFetchSize
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
name|ejbql
argument_list|)
expr_stmt|;
name|ObjectId
name|id
init|=
operator|new
name|ObjectId
argument_list|(
literal|"Artist"
argument_list|,
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|RelationshipQuery
name|relationshipQuery
init|=
operator|new
name|RelationshipQuery
argument_list|(
name|id
argument_list|,
name|Artist
operator|.
name|PAINTING_ARRAY_PROPERTY
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|relationshipQuery
operator|.
name|setStatementFetchSize
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|relationshipQuery
operator|.
name|getMetaData
argument_list|(
name|context
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
operator|.
name|getStatementFetchSize
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
name|relationshipQuery
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

