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
name|Cayenne
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
name|Persistent
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
name|EJBQLQuery
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
name|DataContextEJBQLIsNullTest
extends|extends
name|CayenneCase
block|{
specifier|public
name|void
name|testCompareToNull
parameter_list|()
throws|throws
name|Exception
block|{
comment|// the query below can blow up on FrontBase. See CAY-819 for details.
if|if
condition|(
operator|!
name|getAccessStackAdapter
argument_list|()
operator|.
name|supportsEqualNullSyntax
argument_list|()
condition|)
block|{
return|return;
block|}
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|String
name|ejbql1
init|=
literal|"SELECT p FROM Painting p WHERE p.estimatedPrice = :x"
decl_stmt|;
name|EJBQLQuery
name|query1
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql1
argument_list|)
decl_stmt|;
name|query1
operator|.
name|setParameter
argument_list|(
literal|"x"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// unlike SelectQuery or SQLTemplate, EJBQL nulls are handled just like SQL.
comment|// note that some databases (notable Sybase) actually allow = NULL comparison,
comment|// most do not; per JPA spec the result is undefined.. so we can't make any
comment|// assertions about the result. Just making sure the query doesn't blow up
name|createDataContext
argument_list|()
operator|.
name|performQuery
argument_list|(
name|query1
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCompareToNull2
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|getAccessStackAdapter
argument_list|()
operator|.
name|supportsEqualNullSyntax
argument_list|()
condition|)
block|{
return|return;
block|}
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|String
name|ejbql1
init|=
literal|"SELECT p FROM Painting p WHERE p.toArtist.artistName = :x"
decl_stmt|;
name|EJBQLQuery
name|query1
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql1
argument_list|)
decl_stmt|;
name|query1
operator|.
name|setParameter
argument_list|(
literal|"x"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|createDataContext
argument_list|()
operator|.
name|performQuery
argument_list|(
name|query1
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCompareToNull3
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|getAccessStackAdapter
argument_list|()
operator|.
name|supportsEqualNullSyntax
argument_list|()
condition|)
block|{
return|return;
block|}
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|String
name|ejbql1
init|=
literal|"SELECT p FROM Painting p WHERE :x = p.toArtist.artistName"
decl_stmt|;
name|EJBQLQuery
name|query1
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql1
argument_list|)
decl_stmt|;
name|query1
operator|.
name|setParameter
argument_list|(
literal|"x"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|createDataContext
argument_list|()
operator|.
name|performQuery
argument_list|(
name|query1
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIsNull
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|String
name|ejbql1
init|=
literal|"SELECT p FROM Painting p WHERE p.estimatedPrice IS NULL"
decl_stmt|;
name|EJBQLQuery
name|query1
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql1
argument_list|)
decl_stmt|;
name|List
name|results
init|=
name|createDataContext
argument_list|()
operator|.
name|performQuery
argument_list|(
name|query1
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
literal|33001
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIsNotNull
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|String
name|ejbql1
init|=
literal|"SELECT p FROM Painting p WHERE p.estimatedPrice IS NOT NULL"
decl_stmt|;
name|EJBQLQuery
name|query1
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql1
argument_list|)
decl_stmt|;
name|List
name|results
init|=
name|createDataContext
argument_list|()
operator|.
name|performQuery
argument_list|(
name|query1
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
literal|33002
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testToOneIsNull
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"testToOneIsNull"
argument_list|)
expr_stmt|;
name|String
name|ejbql1
init|=
literal|"SELECT p FROM Painting p WHERE p.toArtist IS NULL"
decl_stmt|;
name|EJBQLQuery
name|query1
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql1
argument_list|)
decl_stmt|;
name|List
name|results
init|=
name|createDataContext
argument_list|()
operator|.
name|performQuery
argument_list|(
name|query1
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
literal|33001
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testToOneIsNotNull
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"testToOneIsNull"
argument_list|)
expr_stmt|;
name|String
name|ejbql1
init|=
literal|"SELECT p FROM Painting p WHERE p.toArtist IS NOT NULL"
decl_stmt|;
name|EJBQLQuery
name|query1
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql1
argument_list|)
decl_stmt|;
name|List
name|results
init|=
name|createDataContext
argument_list|()
operator|.
name|performQuery
argument_list|(
name|query1
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
literal|33003
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

