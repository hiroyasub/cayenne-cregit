begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/* Generated By:JJTree&JavaCC: Do not edit this line. ExpressionParserConstants.java */
end_comment

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
name|exp
operator|.
name|parser
package|;
end_package

begin_comment
comment|/**  * Token literal values and constants.  * Generated by org.javacc.parser.OtherFilesGen#start()  */
end_comment

begin_interface
specifier|public
interface|interface
name|ExpressionParserConstants
block|{
comment|/** End of File. */
name|int
name|EOF
init|=
literal|0
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|NULL
init|=
literal|33
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|TRUE
init|=
literal|34
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|FALSE
init|=
literal|35
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|AVG
init|=
literal|36
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|MIN
init|=
literal|37
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|MAX
init|=
literal|38
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|SUM
init|=
literal|39
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|COUNT
init|=
literal|40
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|CONCAT
init|=
literal|41
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|SUBSTRING
init|=
literal|42
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|TRIM
init|=
literal|43
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|LOWER
init|=
literal|44
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|UPPER
init|=
literal|45
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|LENGTH
init|=
literal|46
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|LOCATE
init|=
literal|47
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|ABS
init|=
literal|48
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|SQRT
init|=
literal|49
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|MOD
init|=
literal|50
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|CURRENT_DATE
init|=
literal|51
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|CURRENT_TIME
init|=
literal|52
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|CURRENT_TIMESTAMP
init|=
literal|53
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|ASTERISK
init|=
literal|58
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|PROPERTY_PATH
init|=
literal|59
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|IDENTIFIER
init|=
literal|60
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|LETTER
init|=
literal|61
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|DIGIT
init|=
literal|62
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|ESC
init|=
literal|65
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|SINGLE_QUOTED_STRING
init|=
literal|67
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|STRING_ESC
init|=
literal|68
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|DOUBLE_QUOTED_STRING
init|=
literal|70
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|INT_LITERAL
init|=
literal|71
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|FLOAT_LITERAL
init|=
literal|72
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|DEC_FLT
init|=
literal|73
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|DEC_DIGITS
init|=
literal|74
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|EXPONENT
init|=
literal|75
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|FLT_SUFF
init|=
literal|76
decl_stmt|;
comment|/** Lexical state. */
name|int
name|DEFAULT
init|=
literal|0
decl_stmt|;
comment|/** Lexical state. */
name|int
name|WithinSingleQuoteLiteral
init|=
literal|1
decl_stmt|;
comment|/** Lexical state. */
name|int
name|WithinDoubleQuoteLiteral
init|=
literal|2
decl_stmt|;
comment|/** Literal token values. */
name|String
index|[]
name|tokenImage
init|=
block|{
literal|"<EOF>"
block|,
literal|"\"or\""
block|,
literal|"\"and\""
block|,
literal|"\"not\""
block|,
literal|"\"!\""
block|,
literal|"\"=\""
block|,
literal|"\"==\""
block|,
literal|"\"!=\""
block|,
literal|"\"<>\""
block|,
literal|"\"<=\""
block|,
literal|"\"<\""
block|,
literal|"\">\""
block|,
literal|"\">=\""
block|,
literal|"\"like\""
block|,
literal|"\"likeIgnoreCase\""
block|,
literal|"\"in\""
block|,
literal|"\"(\""
block|,
literal|"\")\""
block|,
literal|"\"between\""
block|,
literal|"\",\""
block|,
literal|"\"|\""
block|,
literal|"\"^\""
block|,
literal|"\"&\""
block|,
literal|"\"<<\""
block|,
literal|"\">>\""
block|,
literal|"\"+\""
block|,
literal|"\"-\""
block|,
literal|"\"/\""
block|,
literal|"\"~\""
block|,
literal|"\" \""
block|,
literal|"\"\\t\""
block|,
literal|"\"\\n\""
block|,
literal|"\"\\r\""
block|,
literal|"<NULL>"
block|,
literal|"<TRUE>"
block|,
literal|"<FALSE>"
block|,
literal|"\"AVG\""
block|,
literal|"\"MIN\""
block|,
literal|"\"MAX\""
block|,
literal|"\"SUM\""
block|,
literal|"\"COUNT\""
block|,
literal|"\"CONCAT\""
block|,
literal|"\"SUBSTRING\""
block|,
literal|"\"TRIM\""
block|,
literal|"\"LOWER\""
block|,
literal|"\"UPPER\""
block|,
literal|"\"LENGTH\""
block|,
literal|"\"LOCATE\""
block|,
literal|"\"ABS\""
block|,
literal|"\"SQRT\""
block|,
literal|"\"MOD\""
block|,
literal|"\"CURRENT_DATE\""
block|,
literal|"\"CURRENT_TIME\""
block|,
literal|"\"CURRENT_TIMESTAMP\""
block|,
literal|"\"$\""
block|,
literal|"\"obj:\""
block|,
literal|"\"db:\""
block|,
literal|"\"enum:\""
block|,
literal|"\"*\""
block|,
literal|"<PROPERTY_PATH>"
block|,
literal|"<IDENTIFIER>"
block|,
literal|"<LETTER>"
block|,
literal|"<DIGIT>"
block|,
literal|"\"\\\'\""
block|,
literal|"\"\\\"\""
block|,
literal|"<ESC>"
block|,
literal|"<token of kind 66>"
block|,
literal|"\"\\\'\""
block|,
literal|"<STRING_ESC>"
block|,
literal|"<token of kind 69>"
block|,
literal|"\"\\\"\""
block|,
literal|"<INT_LITERAL>"
block|,
literal|"<FLOAT_LITERAL>"
block|,
literal|"<DEC_FLT>"
block|,
literal|"<DEC_DIGITS>"
block|,
literal|"<EXPONENT>"
block|,
literal|"<FLT_SUFF>"
block|,   }
decl_stmt|;
block|}
end_interface

end_unit

