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
name|di
operator|.
name|server
package|;
end_package

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
name|Map
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
name|ConfigurationException
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
name|di
operator|.
name|Provider
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
name|util
operator|.
name|SQLTemplateCustomizer
import|;
end_import

begin_class
specifier|public
class|class
name|SQLTemplateCustomizerProvider
implements|implements
name|Provider
argument_list|<
name|SQLTemplateCustomizer
argument_list|>
block|{
annotation|@
name|Inject
specifier|private
name|DbAdapter
name|dbAdapter
decl_stmt|;
specifier|public
name|SQLTemplateCustomizer
name|get
parameter_list|()
throws|throws
name|ConfigurationException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|q1
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|q1
operator|.
name|put
argument_list|(
literal|"org.apache.cayenne.dba.postgres.PostgresAdapter"
argument_list|,
literal|"SELECT #result('ARTIST_ID'), RTRIM(#result('ARTIST_NAME')), "
operator|+
literal|"#result('DATE_OF_BIRTH') FROM ARTIST ORDER BY ARTIST_ID"
argument_list|)
expr_stmt|;
name|q1
operator|.
name|put
argument_list|(
literal|"org.apache.cayenne.dba.ingres.IngresAdapter"
argument_list|,
literal|"SELECT #result('ARTIST_ID'), TRIM(#result('ARTIST_NAME')), "
operator|+
literal|"#result('DATE_OF_BIRTH') FROM ARTIST ORDER BY ARTIST_ID"
argument_list|)
expr_stmt|;
name|q1
operator|.
name|put
argument_list|(
literal|"org.apache.cayenne.dba.openbase.OpenBaseAdapter"
argument_list|,
literal|"SELECT #result('ARTIST_ID'), #result('ARTIST_NAME'), "
operator|+
literal|"#result('DATE_OF_BIRTH') FROM ARTIST ORDER BY ARTIST_ID"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|q2
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|q2
operator|.
name|put
argument_list|(
literal|"org.apache.cayenne.dba.postgres.PostgresAdapter"
argument_list|,
literal|"SELECT #result('ARTIST_ID'), RTRIM(#result('ARTIST_NAME')), #result('DATE_OF_BIRTH') "
operator|+
literal|"FROM ARTIST WHERE ARTIST_ID = #bind($id)"
argument_list|)
expr_stmt|;
name|q2
operator|.
name|put
argument_list|(
literal|"org.apache.cayenne.dba.ingres.IngresAdapter"
argument_list|,
literal|"SELECT #result('ARTIST_ID'), TRIM(#result('ARTIST_NAME')), #result('DATE_OF_BIRTH') "
operator|+
literal|"FROM ARTIST WHERE ARTIST_ID = #bind($id)"
argument_list|)
expr_stmt|;
name|q2
operator|.
name|put
argument_list|(
literal|"org.apache.cayenne.dba.openbase.OpenBaseAdapter"
argument_list|,
literal|"SELECT #result('ARTIST_ID'), #result('ARTIST_NAME'), #result('DATE_OF_BIRTH') "
operator|+
literal|"FROM ARTIST WHERE ARTIST_ID = #bind($id)"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|q3
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|q3
operator|.
name|put
argument_list|(
literal|"org.apache.cayenne.dba.oracle.OracleAdapter"
argument_list|,
literal|"UPDATE ARTIST SET ARTIST_NAME = #bind($newName) WHERE RTRIM(ARTIST_NAME) = #bind($oldName)"
argument_list|)
expr_stmt|;
name|q3
operator|.
name|put
argument_list|(
literal|"org.apache.cayenne.dba.oracle.Oracle8Adapter"
argument_list|,
literal|"UPDATE ARTIST SET ARTIST_NAME = #bind($newName) WHERE RTRIM(ARTIST_NAME) = #bind($oldName)"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"SELECT * FROM ARTIST ORDER BY ARTIST_ID"
argument_list|,
name|q1
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"SELECT * FROM ARTIST WHERE ARTIST_ID = #bind($id)"
argument_list|,
name|q2
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"UPDATE ARTIST SET ARTIST_NAME = #bind($newName) "
operator|+
literal|"WHERE ARTIST_NAME = #bind($oldName)"
argument_list|,
name|q3
argument_list|)
expr_stmt|;
return|return
operator|new
name|SQLTemplateCustomizer
argument_list|(
name|map
argument_list|,
name|dbAdapter
argument_list|)
return|;
block|}
block|}
end_class

end_unit
