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
name|configuration
operator|.
name|web
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
name|configuration
operator|.
name|CayenneRuntime
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mockrunner
operator|.
name|mock
operator|.
name|web
operator|.
name|MockServletContext
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
specifier|public
class|class
name|WebUtilTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testGetCayenneRuntime
parameter_list|()
block|{
name|MockServletContext
name|context
init|=
operator|new
name|MockServletContext
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|WebUtil
operator|.
name|getCayenneRuntime
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|CayenneRuntime
name|runtime
init|=
operator|new
name|CayenneRuntime
argument_list|(
literal|"xx"
argument_list|)
block|{         }
decl_stmt|;
name|WebUtil
operator|.
name|setCayenneRuntime
argument_list|(
name|context
argument_list|,
name|runtime
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|runtime
argument_list|,
name|WebUtil
operator|.
name|getCayenneRuntime
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|CayenneRuntime
name|runtime1
init|=
operator|new
name|CayenneRuntime
argument_list|(
literal|"yy"
argument_list|)
block|{         }
decl_stmt|;
name|WebUtil
operator|.
name|setCayenneRuntime
argument_list|(
name|context
argument_list|,
name|runtime1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|runtime1
argument_list|,
name|WebUtil
operator|.
name|getCayenneRuntime
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|WebUtil
operator|.
name|setCayenneRuntime
argument_list|(
name|context
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|WebUtil
operator|.
name|getCayenneRuntime
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

