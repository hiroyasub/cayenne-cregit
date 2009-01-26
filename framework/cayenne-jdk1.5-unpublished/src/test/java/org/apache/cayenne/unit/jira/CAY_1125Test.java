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
operator|.
name|jira
package|;
end_package

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
name|art
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
name|ObjAttribute
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
name|ObjEntity
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
name|CapsStrategy
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
name|SQLTemplate
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
name|CayenneCase
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
name|MySQLStackAdapter
import|;
end_import

begin_class
specifier|public
class|class
name|CAY_1125Test
extends|extends
name|CayenneCase
block|{
specifier|private
name|boolean
name|isMySQL
parameter_list|()
block|{
return|return
name|getAccessStackAdapter
argument_list|()
operator|instanceof
name|MySQLStackAdapter
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
if|if
condition|(
name|isMySQL
argument_list|()
condition|)
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|createDataContext
argument_list|()
operator|.
name|performGenericQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"alter table ARTIST ADD COLUMN SMALLINT_UNSIGNED SMALLINT UNSIGNED NULL"
argument_list|)
argument_list|)
expr_stmt|;
name|DbEntity
name|artistDB
init|=
name|getDbEntity
argument_list|(
literal|"ARTIST"
argument_list|)
decl_stmt|;
name|artistDB
operator|.
name|addAttribute
argument_list|(
operator|new
name|DbAttribute
argument_list|(
literal|"SMALLINT_UNSIGNED"
argument_list|,
name|Types
operator|.
name|SMALLINT
argument_list|,
name|artistDB
argument_list|)
argument_list|)
expr_stmt|;
name|ObjEntity
name|artistObj
init|=
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|ObjAttribute
name|artistObjAttr
init|=
operator|new
name|ObjAttribute
argument_list|(
literal|"smallintUnsigned"
argument_list|,
literal|"java.lang.Integer"
argument_list|,
name|artistObj
argument_list|)
decl_stmt|;
name|artistObjAttr
operator|.
name|setDbAttributePath
argument_list|(
literal|"SMALLINT_UNSIGNED"
argument_list|)
expr_stmt|;
name|artistObj
operator|.
name|addAttribute
argument_list|(
name|artistObjAttr
argument_list|)
expr_stmt|;
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|clearCache
argument_list|()
expr_stmt|;
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptorMap
argument_list|()
operator|.
name|clearDescriptors
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|isMySQL
argument_list|()
condition|)
block|{
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
operator|.
name|removeAttribute
argument_list|(
literal|"smallintUnsigned"
argument_list|)
expr_stmt|;
name|getDbEntity
argument_list|(
literal|"ARTIST"
argument_list|)
operator|.
name|removeAttribute
argument_list|(
literal|"SMALLINT_UNSIGNED"
argument_list|)
expr_stmt|;
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|clearCache
argument_list|()
expr_stmt|;
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptorMap
argument_list|()
operator|.
name|clearDescriptors
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testSQLTemplate
parameter_list|()
block|{
if|if
condition|(
name|isMySQL
argument_list|()
condition|)
block|{
name|SQLTemplate
name|insert
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"INSERT INTO ARTIST (ARTIST_ID, ARTIST_NAME, SMALLINT_UNSIGNED) VALUES (1, 'A', 33000)"
argument_list|)
decl_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|performGenericQuery
argument_list|(
name|insert
argument_list|)
expr_stmt|;
name|SQLTemplate
name|select
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"SELECT * FROM ARTIST"
argument_list|)
decl_stmt|;
name|select
operator|.
name|setColumnNamesCapitalization
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|33000
argument_list|,
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|readProperty
argument_list|(
literal|"smallintUnsigned"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

