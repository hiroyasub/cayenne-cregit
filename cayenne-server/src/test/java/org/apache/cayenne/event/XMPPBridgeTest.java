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
name|event
package|;
end_package

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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
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
name|assertTrue
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|XMPPBridgeTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testEventSerialization
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
name|info
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|info
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|CayenneEvent
name|e
init|=
operator|new
name|CayenneEvent
argument_list|(
name|this
argument_list|,
name|this
argument_list|,
name|info
argument_list|)
decl_stmt|;
name|String
name|string
init|=
name|XMPPBridge
operator|.
name|serializeToString
argument_list|(
name|e
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|string
argument_list|)
expr_stmt|;
name|Object
name|copy
init|=
name|XMPPBridge
operator|.
name|deserializeFromString
argument_list|(
name|string
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|copy
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|copy
operator|instanceof
name|CayenneEvent
argument_list|)
expr_stmt|;
name|CayenneEvent
name|e2
init|=
operator|(
name|CayenneEvent
operator|)
name|copy
decl_stmt|;
name|assertEquals
argument_list|(
name|info
argument_list|,
name|e2
operator|.
name|getInfo
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|e2
operator|.
name|getPostedBy
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|e2
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

