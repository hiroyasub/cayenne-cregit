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
name|swing
operator|.
name|components
operator|.
name|textpane
operator|.
name|syntax
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Color
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Font
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|SyntaxConstant
block|{
specifier|public
specifier|static
specifier|final
name|Font
name|DEFAULT_FONT
decl_stmt|;
static|static
block|{
name|String
name|fontName
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|toLowerCase
argument_list|()
operator|.
name|contains
argument_list|(
literal|"win"
argument_list|)
condition|?
literal|"Courier New"
else|:
literal|"Courier"
decl_stmt|;
name|DEFAULT_FONT
operator|=
operator|new
name|Font
argument_list|(
name|fontName
argument_list|,
name|Font
operator|.
name|PLAIN
argument_list|,
literal|14
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
specifier|final
name|Color
name|DEFAULT_COLOR
init|=
name|Color
operator|.
name|black
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|COMMENT_TEXT
init|=
literal|"(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|COMMENT_TEXT_START
init|=
literal|"/\\*.?"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|STRING_TEXT
init|=
literal|"'[^']*'"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NUMBER_TEXT
init|=
literal|"\\d+"
decl_stmt|;
specifier|public
specifier|abstract
name|String
index|[]
name|getKEYWORDS
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|String
index|[]
name|getKEYWORDS2
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|String
index|[]
name|getTYPES
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|String
index|[]
name|getOPERATORS
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|String
name|getContentType
parameter_list|()
function_decl|;
block|}
end_class

end_unit

