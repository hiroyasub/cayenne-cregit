begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/* Generated By:JJTree&JavaCC: Do not edit this line. SQLTemplateParserConstants.java */
end_comment

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
name|template
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
name|SQLTemplateParserConstants
block|{
comment|/** End of File. */
name|int
name|EOF
init|=
literal|0
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|IF
init|=
literal|5
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|ELSE
init|=
literal|6
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|END
init|=
literal|7
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|SHARP
init|=
literal|8
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|DOLLAR
init|=
literal|9
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|TRUE
init|=
literal|10
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|FALSE
init|=
literal|11
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|RBRACKET
init|=
literal|12
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|COMMA
init|=
literal|13
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|LSBRACKET
init|=
literal|14
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|RSBRACKET
init|=
literal|15
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|LBRACKET
init|=
literal|16
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|DOT
init|=
literal|17
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|IDENTIFIER
init|=
literal|18
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|LETTER
init|=
literal|19
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|DIGIT
init|=
literal|20
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|SINGLE_LINE_COMMENT_END
init|=
literal|22
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|ESC
init|=
literal|26
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|SINGLE_QUOTED_STRING
init|=
literal|28
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|STRING_ESC
init|=
literal|29
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|DOUBLE_QUOTED_STRING
init|=
literal|31
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|INT_LITERAL
init|=
literal|32
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|FLOAT_LITERAL
init|=
literal|33
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|DEC_FLT
init|=
literal|34
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|DEC_DIGITS
init|=
literal|35
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|EXPONENT
init|=
literal|36
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|FLT_SUFF
init|=
literal|37
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|TEXT
init|=
literal|38
decl_stmt|;
comment|/** RegularExpression Id. */
name|int
name|TEXT_OTHER
init|=
literal|39
decl_stmt|;
comment|/** Lexical state. */
name|int
name|DEFAULT
init|=
literal|0
decl_stmt|;
comment|/** Lexical state. */
name|int
name|ARGS
init|=
literal|1
decl_stmt|;
comment|/** Lexical state. */
name|int
name|NOT_TEXT
init|=
literal|2
decl_stmt|;
comment|/** Lexical state. */
name|int
name|IN_SINGLE_LINE_COMMENT
init|=
literal|3
decl_stmt|;
comment|/** Lexical state. */
name|int
name|WithinSingleQuoteLiteral
init|=
literal|4
decl_stmt|;
comment|/** Lexical state. */
name|int
name|WithinDoubleQuoteLiteral
init|=
literal|5
decl_stmt|;
comment|/** Literal token values. */
name|String
index|[]
name|tokenImage
init|=
block|{
literal|"<EOF>"
block|,
literal|"\" \""
block|,
literal|"\"\\t\""
block|,
literal|"\"\\n\""
block|,
literal|"\"\\r\""
block|,
literal|"\"#if\""
block|,
literal|"\"#else\""
block|,
literal|"\"#end\""
block|,
literal|"\"#\""
block|,
literal|"\"$\""
block|,
literal|"<TRUE>"
block|,
literal|"<FALSE>"
block|,
literal|"\")\""
block|,
literal|"\",\""
block|,
literal|"\"[\""
block|,
literal|"\"]\""
block|,
literal|"\"(\""
block|,
literal|"\".\""
block|,
literal|"<IDENTIFIER>"
block|,
literal|"<LETTER>"
block|,
literal|"<DIGIT>"
block|,
literal|"\"##\""
block|,
literal|"<SINGLE_LINE_COMMENT_END>"
block|,
literal|"<token of kind 23>"
block|,
literal|"\"\\\'\""
block|,
literal|"\"\\\"\""
block|,
literal|"<ESC>"
block|,
literal|"<token of kind 27>"
block|,
literal|"\"\\\'\""
block|,
literal|"<STRING_ESC>"
block|,
literal|"<token of kind 30>"
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
block|,
literal|"<TEXT>"
block|,
literal|"<TEXT_OTHER>"
block|,   }
decl_stmt|;
block|}
end_interface

end_unit

