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
name|syntax
package|;
end_package

begin_class
specifier|public
class|class
name|EJBQLSyntaxConstant
extends|extends
name|SyntaxConstant
block|{
specifier|private
specifier|static
name|String
index|[]
name|KEYWORDS
init|=
block|{
literal|"AS"
block|,
literal|"ABS"
block|,
literal|"ASC"
block|,
literal|"AVG"
block|,
literal|"BETWEEN"
block|,
literal|"BOTH"
block|,
literal|"BIT_LENGTH"
block|,
literal|"CHARACTER_LENGTH"
block|,
literal|"CHAR_LENGTH"
block|,
literal|"COUNT"
block|,
literal|"CONCAT"
block|,
literal|"CURRENT_TIME"
block|,
literal|"CURRENT_DATE"
block|,
literal|"CURRENT_TIMESTAMP"
block|,
literal|"DELETE"
block|,
literal|"DESC"
block|,
literal|"DISTINCT"
block|,
literal|"EMPTY"
block|,
literal|"ESCAPE"
block|,
literal|"FALSE"
block|,
literal|"FETCH"
block|,
literal|"FROM"
block|,
literal|"GROUP"
block|,
literal|"HAVING"
block|,
literal|"IS"
block|,
literal|"INNER"
block|,
literal|"LOCATE"
block|,
literal|"LOWER"
block|,
literal|"LEADING"
block|,
literal|"LEFT"
block|,
literal|"LENGTH"
block|,
literal|"MAX"
block|,
literal|"MEMBER"
block|,
literal|"MIN"
block|,
literal|"MOD"
block|,
literal|"NEW"
block|,
literal|"NULL"
block|,
literal|"OBJECT"
block|,
literal|"OF"
block|,
literal|"ORDER"
block|,
literal|"POSITION"
block|,
literal|"SELECT"
block|,
literal|"SOME"
block|,
literal|"SUM"
block|,
literal|"SIZE"
block|,
literal|"SQRT"
block|,
literal|"SUBSTR"
block|,
literal|"TRAILING"
block|,
literal|"TRUE"
block|,
literal|"TRIM"
block|,
literal|"UNKNOWN"
block|,
literal|"UPDATE"
block|,
literal|"UPPER"
block|,
literal|"USER"
block|,
literal|"WHERE"
block|,
literal|"JOIN"
block|}
decl_stmt|;
specifier|private
specifier|static
name|String
index|[]
name|KEYWORDS2
init|=
block|{}
decl_stmt|;
specifier|private
specifier|static
name|String
index|[]
name|TYPES
init|=
block|{}
decl_stmt|;
specifier|private
specifier|static
name|String
index|[]
name|OPERATORS
init|=
block|{
literal|"ALL"
block|,
literal|"AND"
block|,
literal|"ANY"
block|,
literal|"BETWEEN"
block|,
literal|"BY"
block|,
literal|"EXISTS"
block|,
literal|"IN"
block|,
literal|"LIKE"
block|,
literal|"NOT"
block|,
literal|"NULL"
block|,
literal|"OR"
block|}
decl_stmt|;
specifier|public
name|String
index|[]
name|getKEYWORDS
parameter_list|()
block|{
return|return
name|KEYWORDS
return|;
block|}
specifier|public
name|String
index|[]
name|getKEYWORDS2
parameter_list|()
block|{
return|return
name|KEYWORDS2
return|;
block|}
specifier|public
name|String
index|[]
name|getTYPES
parameter_list|()
block|{
return|return
name|TYPES
return|;
block|}
specifier|public
name|String
index|[]
name|getOPERATORS
parameter_list|()
block|{
return|return
name|OPERATORS
return|;
block|}
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
literal|"text/ejbql"
return|;
block|}
block|}
end_class

end_unit

