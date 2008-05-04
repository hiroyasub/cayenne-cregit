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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|art
operator|.
name|Gallery
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
name|DataObjectUtils
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
name|exp
operator|.
name|ExpressionFactory
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
name|unit
operator|.
name|CayenneCase
import|;
end_import

begin_class
specifier|public
class|class
name|DataContextJoinAliasesTest
extends|extends
name|CayenneCase
block|{
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
name|deleteTestData
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testMatchAll
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Date
name|now
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"date1"
argument_list|,
name|now
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"date2"
argument_list|,
name|now
argument_list|)
expr_stmt|;
name|createTestData
argument_list|(
literal|"testMatchAll"
argument_list|,
name|params
argument_list|)
expr_stmt|;
comment|// select all galleries that have exhibits by both Picasso and Dali...
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Artist
name|picasso
init|=
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Artist
name|dali
init|=
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Gallery
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|path
init|=
literal|"exhibitArray.artistExhibitArray"
decl_stmt|;
name|query
operator|.
name|aliasJoin
argument_list|(
literal|"p"
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|query
operator|.
name|aliasJoin
argument_list|(
literal|"d"
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|query
operator|.
name|andQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"p.toArtist"
argument_list|,
name|picasso
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|.
name|andQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"d.toArtist"
argument_list|,
name|dali
argument_list|)
argument_list|)
expr_stmt|;
comment|//        List<Gallery> galleries = context.performQuery(query);
comment|//
comment|//        assertEquals(1, galleries.size());
comment|//        assertEquals("G1", galleries.get(0).getGalleryName());
block|}
block|}
end_class

end_unit

