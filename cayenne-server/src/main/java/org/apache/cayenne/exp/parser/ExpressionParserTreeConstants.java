begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/* Generated By:JavaCC: Do not edit this line. ExpressionParserTreeConstants.java Version 5.0 */
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

begin_interface
specifier|public
interface|interface
name|ExpressionParserTreeConstants
block|{
specifier|public
name|int
name|JJTVOID
init|=
literal|0
decl_stmt|;
specifier|public
name|int
name|JJTOR
init|=
literal|1
decl_stmt|;
specifier|public
name|int
name|JJTAND
init|=
literal|2
decl_stmt|;
specifier|public
name|int
name|JJTNOT
init|=
literal|3
decl_stmt|;
specifier|public
name|int
name|JJTTRUE
init|=
literal|4
decl_stmt|;
specifier|public
name|int
name|JJTFALSE
init|=
literal|5
decl_stmt|;
specifier|public
name|int
name|JJTEQUAL
init|=
literal|6
decl_stmt|;
specifier|public
name|int
name|JJTNOTEQUAL
init|=
literal|7
decl_stmt|;
specifier|public
name|int
name|JJTLESSOREQUAL
init|=
literal|8
decl_stmt|;
specifier|public
name|int
name|JJTLESS
init|=
literal|9
decl_stmt|;
specifier|public
name|int
name|JJTGREATER
init|=
literal|10
decl_stmt|;
specifier|public
name|int
name|JJTGREATEROREQUAL
init|=
literal|11
decl_stmt|;
specifier|public
name|int
name|JJTLIKE
init|=
literal|12
decl_stmt|;
specifier|public
name|int
name|JJTLIKEIGNORECASE
init|=
literal|13
decl_stmt|;
specifier|public
name|int
name|JJTIN
init|=
literal|14
decl_stmt|;
specifier|public
name|int
name|JJTBETWEEN
init|=
literal|15
decl_stmt|;
specifier|public
name|int
name|JJTNOTLIKE
init|=
literal|16
decl_stmt|;
specifier|public
name|int
name|JJTNOTLIKEIGNORECASE
init|=
literal|17
decl_stmt|;
specifier|public
name|int
name|JJTNOTIN
init|=
literal|18
decl_stmt|;
specifier|public
name|int
name|JJTNOTBETWEEN
init|=
literal|19
decl_stmt|;
specifier|public
name|int
name|JJTLIST
init|=
literal|20
decl_stmt|;
specifier|public
name|int
name|JJTSCALAR
init|=
literal|21
decl_stmt|;
specifier|public
name|int
name|JJTBITWISEOR
init|=
literal|22
decl_stmt|;
specifier|public
name|int
name|JJTBITWISEXOR
init|=
literal|23
decl_stmt|;
specifier|public
name|int
name|JJTBITWISEAND
init|=
literal|24
decl_stmt|;
specifier|public
name|int
name|JJTBITWISELEFTSHIFT
init|=
literal|25
decl_stmt|;
specifier|public
name|int
name|JJTBITWISERIGHTSHIFT
init|=
literal|26
decl_stmt|;
specifier|public
name|int
name|JJTADD
init|=
literal|27
decl_stmt|;
specifier|public
name|int
name|JJTSUBTRACT
init|=
literal|28
decl_stmt|;
specifier|public
name|int
name|JJTMULTIPLY
init|=
literal|29
decl_stmt|;
specifier|public
name|int
name|JJTDIVIDE
init|=
literal|30
decl_stmt|;
specifier|public
name|int
name|JJTBITWISENOT
init|=
literal|31
decl_stmt|;
specifier|public
name|int
name|JJTNEGATE
init|=
literal|32
decl_stmt|;
specifier|public
name|int
name|JJTCONCAT
init|=
literal|33
decl_stmt|;
specifier|public
name|int
name|JJTSUBSTRING
init|=
literal|34
decl_stmt|;
specifier|public
name|int
name|JJTTRIM
init|=
literal|35
decl_stmt|;
specifier|public
name|int
name|JJTLOWER
init|=
literal|36
decl_stmt|;
specifier|public
name|int
name|JJTUPPER
init|=
literal|37
decl_stmt|;
specifier|public
name|int
name|JJTLENGTH
init|=
literal|38
decl_stmt|;
specifier|public
name|int
name|JJTLOCATE
init|=
literal|39
decl_stmt|;
specifier|public
name|int
name|JJTABS
init|=
literal|40
decl_stmt|;
specifier|public
name|int
name|JJTSQRT
init|=
literal|41
decl_stmt|;
specifier|public
name|int
name|JJTMOD
init|=
literal|42
decl_stmt|;
specifier|public
name|int
name|JJTASTERISK
init|=
literal|43
decl_stmt|;
specifier|public
name|int
name|JJTCOUNT
init|=
literal|44
decl_stmt|;
specifier|public
name|int
name|JJTAVG
init|=
literal|45
decl_stmt|;
specifier|public
name|int
name|JJTMAX
init|=
literal|46
decl_stmt|;
specifier|public
name|int
name|JJTMIN
init|=
literal|47
decl_stmt|;
specifier|public
name|int
name|JJTSUM
init|=
literal|48
decl_stmt|;
specifier|public
name|int
name|JJTCURRENTDATE
init|=
literal|49
decl_stmt|;
specifier|public
name|int
name|JJTCURRENTTIME
init|=
literal|50
decl_stmt|;
specifier|public
name|int
name|JJTCURRENTTIMESTAMP
init|=
literal|51
decl_stmt|;
specifier|public
name|int
name|JJTNAMEDPARAMETER
init|=
literal|52
decl_stmt|;
specifier|public
name|int
name|JJTOBJPATH
init|=
literal|53
decl_stmt|;
specifier|public
name|int
name|JJTDBPATH
init|=
literal|54
decl_stmt|;
specifier|public
name|String
index|[]
name|jjtNodeName
init|=
block|{
literal|"void"
block|,
literal|"Or"
block|,
literal|"And"
block|,
literal|"Not"
block|,
literal|"True"
block|,
literal|"False"
block|,
literal|"Equal"
block|,
literal|"NotEqual"
block|,
literal|"LessOrEqual"
block|,
literal|"Less"
block|,
literal|"Greater"
block|,
literal|"GreaterOrEqual"
block|,
literal|"Like"
block|,
literal|"LikeIgnoreCase"
block|,
literal|"In"
block|,
literal|"Between"
block|,
literal|"NotLike"
block|,
literal|"NotLikeIgnoreCase"
block|,
literal|"NotIn"
block|,
literal|"NotBetween"
block|,
literal|"List"
block|,
literal|"Scalar"
block|,
literal|"BitwiseOr"
block|,
literal|"BitwiseXor"
block|,
literal|"BitwiseAnd"
block|,
literal|"BitwiseLeftShift"
block|,
literal|"BitwiseRightShift"
block|,
literal|"Add"
block|,
literal|"Subtract"
block|,
literal|"Multiply"
block|,
literal|"Divide"
block|,
literal|"BitwiseNot"
block|,
literal|"Negate"
block|,
literal|"Concat"
block|,
literal|"Substring"
block|,
literal|"Trim"
block|,
literal|"Lower"
block|,
literal|"Upper"
block|,
literal|"Length"
block|,
literal|"Locate"
block|,
literal|"Abs"
block|,
literal|"Sqrt"
block|,
literal|"Mod"
block|,
literal|"Asterisk"
block|,
literal|"Count"
block|,
literal|"Avg"
block|,
literal|"Max"
block|,
literal|"Min"
block|,
literal|"Sum"
block|,
literal|"CurrentDate"
block|,
literal|"CurrentTime"
block|,
literal|"CurrentTimestamp"
block|,
literal|"NamedParameter"
block|,
literal|"ObjPath"
block|,
literal|"DbPath"
block|,   }
decl_stmt|;
block|}
end_interface

begin_comment
comment|/* JavaCC - OriginalChecksum=7571de31b81c878cf4b5f52ebb555fb1 (do not edit this line) */
end_comment

end_unit

