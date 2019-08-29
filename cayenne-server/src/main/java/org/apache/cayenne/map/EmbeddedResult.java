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
name|map
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

begin_comment
comment|/**  * A metadata object that provides mapping of a set of result columns to an Embeddable object.  * Used by {@link SQLResult}.  * Note that fields in the EmbeddedResult are not required to follow the order of columns  * in the actual query, and can be added in the arbitrary order.  *  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|EmbeddedResult
block|{
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fields
decl_stmt|;
specifier|private
specifier|final
name|Embeddable
name|embeddable
decl_stmt|;
specifier|public
name|EmbeddedResult
parameter_list|(
name|Embeddable
name|embeddable
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|this
operator|.
name|embeddable
operator|=
name|embeddable
expr_stmt|;
name|this
operator|.
name|fields
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
operator|(
name|int
operator|)
name|Math
operator|.
name|ceil
argument_list|(
name|size
operator|/
literal|0.75
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addAttribute
parameter_list|(
name|ObjAttribute
name|attr
parameter_list|)
block|{
name|fields
operator|.
name|put
argument_list|(
name|attr
operator|.
name|getDbAttributePath
argument_list|()
argument_list|,
name|getAttributeName
argument_list|(
name|attr
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|String
name|getAttributeName
parameter_list|(
name|ObjAttribute
name|attr
parameter_list|)
block|{
name|String
name|name
init|=
name|attr
operator|.
name|getName
argument_list|()
decl_stmt|;
return|return
name|name
operator|.
name|substring
argument_list|(
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
operator|+
literal|1
argument_list|)
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getFields
parameter_list|()
block|{
return|return
name|fields
return|;
block|}
specifier|public
name|Embeddable
name|getEmbeddable
parameter_list|()
block|{
return|return
name|embeddable
return|;
block|}
block|}
end_class

end_unit

