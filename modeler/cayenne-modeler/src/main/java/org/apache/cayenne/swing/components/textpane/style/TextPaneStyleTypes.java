begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|style
package|;
end_package

begin_enum
specifier|public
enum|enum
name|TextPaneStyleTypes
block|{
name|KEYWORDS
block|,
comment|// Language keywords
name|KEYWORDS2
block|,
comment|// Language keywords with style2
name|OPERATORS
block|,
comment|// Language operators
name|NUMBER
block|,
comment|// numbers in various formats
name|STRING
block|,
comment|// String
name|COMMENT
block|,
comment|// comments
name|TYPE
comment|// Types, usually not keywords, but supported by the language
block|}
end_enum

end_unit

