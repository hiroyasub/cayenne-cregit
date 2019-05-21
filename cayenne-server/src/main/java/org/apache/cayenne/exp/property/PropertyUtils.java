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
name|exp
operator|.
name|property
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
name|exp
operator|.
name|parser
operator|.
name|ASTObjPath
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
name|exp
operator|.
name|parser
operator|.
name|ASTPath
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
class|class
name|PropertyUtils
block|{
specifier|static
name|ASTPath
name|createPathExp
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|alias
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|aliasMap
parameter_list|)
block|{
name|int
name|index
init|=
name|path
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
decl_stmt|;
name|String
name|aliasedPath
init|=
name|index
operator|!=
operator|-
literal|1
condition|?
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
operator|+
literal|1
argument_list|)
operator|+
name|alias
else|:
name|alias
decl_stmt|;
name|String
name|segmentPath
init|=
name|path
operator|.
name|substring
argument_list|(
name|index
operator|!=
operator|-
literal|1
condition|?
name|index
operator|+
literal|1
else|:
literal|0
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|pathAliases
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|aliasMap
argument_list|)
decl_stmt|;
name|pathAliases
operator|.
name|put
argument_list|(
name|alias
argument_list|,
name|segmentPath
argument_list|)
expr_stmt|;
return|return
name|buildExp
argument_list|(
name|aliasedPath
argument_list|,
name|pathAliases
argument_list|)
return|;
block|}
specifier|static
name|ASTPath
name|buildExp
parameter_list|(
name|String
name|path
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|pathAliases
parameter_list|)
block|{
name|ASTPath
name|pathExp
init|=
operator|new
name|ASTObjPath
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|pathExp
operator|.
name|setPathAliases
argument_list|(
name|pathAliases
argument_list|)
expr_stmt|;
return|return
name|pathExp
return|;
block|}
block|}
end_class

end_unit

