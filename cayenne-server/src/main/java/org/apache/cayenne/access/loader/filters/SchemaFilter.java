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
name|access
operator|.
name|loader
operator|.
name|filters
package|;
end_package

begin_comment
comment|/** * @since 4.0. */
end_comment

begin_class
specifier|public
class|class
name|SchemaFilter
block|{
specifier|public
specifier|final
name|String
name|name
decl_stmt|;
specifier|public
specifier|final
name|TableFilter
name|tables
decl_stmt|;
specifier|public
specifier|final
name|PatternFilter
name|procedures
decl_stmt|;
specifier|public
name|SchemaFilter
parameter_list|(
name|String
name|name
parameter_list|,
name|TableFilter
name|tables
parameter_list|,
name|PatternFilter
name|procedures
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|tables
operator|=
name|tables
expr_stmt|;
name|this
operator|.
name|procedures
operator|=
name|procedures
expr_stmt|;
block|}
specifier|protected
name|StringBuilder
name|toString
parameter_list|(
name|StringBuilder
name|res
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
name|res
operator|.
name|append
argument_list|(
name|prefix
argument_list|)
operator|.
name|append
argument_list|(
literal|"Schema: "
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|tables
operator|.
name|toString
argument_list|(
name|res
argument_list|,
name|prefix
operator|+
literal|"  "
argument_list|)
expr_stmt|;
name|res
operator|.
name|append
argument_list|(
name|prefix
argument_list|)
operator|.
name|append
argument_list|(
literal|"  Procedures: "
argument_list|)
expr_stmt|;
name|procedures
operator|.
name|toString
argument_list|(
name|res
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
return|return
name|res
return|;
block|}
block|}
end_class

end_unit

