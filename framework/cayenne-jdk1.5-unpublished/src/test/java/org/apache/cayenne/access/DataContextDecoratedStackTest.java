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
name|DataChannel
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
name|QueryResponse
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
name|configuration
operator|.
name|server
operator|.
name|ServerRuntime
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
name|frontbase
operator|.
name|FrontBaseAdapter
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
name|openbase
operator|.
name|OpenBaseAdapter
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
name|event
operator|.
name|EventManager
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
name|graph
operator|.
name|GraphDiff
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
name|EntityResolver
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
name|Query
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
name|DataContextDecoratedStackTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ServerRuntime
name|runtime
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
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
literal|"PAINTING_INFO"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"PAINTING"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ARTIST_EXHIBIT"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ARTIST_GROUP"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ARTIST"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCommitDecorated
parameter_list|()
block|{
name|DataDomain
name|dd
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
decl_stmt|;
name|DataChannel
name|decorator
init|=
operator|new
name|DataChannelDecorator
argument_list|(
name|dd
argument_list|)
decl_stmt|;
name|DataContext
name|context
init|=
operator|new
name|DataContext
argument_list|(
name|decorator
argument_list|,
operator|new
name|ObjectStore
argument_list|(
name|dd
operator|.
name|getSharedSnapshotCache
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Artist
name|a
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a
operator|.
name|setArtistName
argument_list|(
literal|"XXX"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"select #result('count(1)' 'int' 'x') from ARTIST"
argument_list|)
decl_stmt|;
name|query
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|query
operator|.
name|setTemplate
argument_list|(
name|FrontBaseAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"select #result('COUNT(ARTIST_ID)' 'int' 'x') from ARTIST"
argument_list|)
expr_stmt|;
name|query
operator|.
name|setTemplate
argument_list|(
name|OpenBaseAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"select #result('COUNT(ARTIST_ID)' 'int' 'x') from ARTIST"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|count
init|=
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|,
name|count
operator|.
name|get
argument_list|(
literal|"x"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetParentDataDomain
parameter_list|()
block|{
name|DataDomain
name|dd
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
decl_stmt|;
name|DataChannel
name|decorator
init|=
operator|new
name|DataChannelDecorator
argument_list|(
name|dd
argument_list|)
decl_stmt|;
name|DataContext
name|context
init|=
operator|new
name|DataContext
argument_list|(
name|decorator
argument_list|,
operator|new
name|ObjectStore
argument_list|(
name|dd
operator|.
name|getSharedSnapshotCache
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|dd
argument_list|,
name|context
operator|.
name|getParentDataDomain
argument_list|()
argument_list|)
expr_stmt|;
block|}
class|class
name|DataChannelDecorator
implements|implements
name|DataChannel
block|{
specifier|protected
name|DataChannel
name|channel
decl_stmt|;
specifier|protected
name|DataChannelDecorator
parameter_list|()
block|{
block|}
specifier|public
name|DataChannelDecorator
parameter_list|(
name|DataChannel
name|channel
parameter_list|)
block|{
name|setChannel
argument_list|(
name|channel
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EntityResolver
name|getEntityResolver
parameter_list|()
block|{
return|return
name|channel
operator|.
name|getEntityResolver
argument_list|()
return|;
block|}
specifier|public
name|EventManager
name|getEventManager
parameter_list|()
block|{
return|return
name|channel
operator|.
name|getEventManager
argument_list|()
return|;
block|}
specifier|public
name|QueryResponse
name|onQuery
parameter_list|(
name|ObjectContext
name|originatingContext
parameter_list|,
name|Query
name|query
parameter_list|)
block|{
return|return
name|channel
operator|.
name|onQuery
argument_list|(
name|originatingContext
argument_list|,
name|query
argument_list|)
return|;
block|}
specifier|public
name|GraphDiff
name|onSync
parameter_list|(
name|ObjectContext
name|originatingContext
parameter_list|,
name|GraphDiff
name|changes
parameter_list|,
name|int
name|syncType
parameter_list|)
block|{
return|return
name|channel
operator|.
name|onSync
argument_list|(
name|originatingContext
argument_list|,
name|changes
argument_list|,
name|syncType
argument_list|)
return|;
block|}
specifier|public
name|DataChannel
name|getChannel
parameter_list|()
block|{
return|return
name|channel
return|;
block|}
specifier|public
name|void
name|setChannel
parameter_list|(
name|DataChannel
name|channel
parameter_list|)
block|{
name|this
operator|.
name|channel
operator|=
name|channel
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

