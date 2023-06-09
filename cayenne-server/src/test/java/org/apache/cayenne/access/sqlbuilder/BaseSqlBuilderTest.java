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
name|access
operator|.
name|sqlbuilder
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
name|access
operator|.
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|Node
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
class|class
name|BaseSqlBuilderTest
block|{
name|void
name|assertSQL
parameter_list|(
name|String
name|expected
parameter_list|,
name|Node
name|node
parameter_list|)
block|{
name|assertSQL
argument_list|(
name|expected
argument_list|,
name|node
argument_list|,
operator|new
name|StringBuilderAppendable
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|void
name|assertQuotedSQL
parameter_list|(
name|String
name|expected
parameter_list|,
name|Node
name|node
parameter_list|)
block|{
name|assertSQL
argument_list|(
name|expected
argument_list|,
name|node
argument_list|,
operator|new
name|MockQuotedStringBuilderAppendable
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|void
name|assertSQL
parameter_list|(
name|String
name|expected
parameter_list|,
name|Node
name|node
parameter_list|,
name|QuotingAppendable
name|appendable
parameter_list|)
block|{
name|SQLGenerationVisitor
name|visitor
init|=
operator|new
name|SQLGenerationVisitor
argument_list|(
name|appendable
argument_list|)
decl_stmt|;
name|node
operator|.
name|visit
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|visitor
operator|.
name|getSQLString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|static
class|class
name|MockQuotedStringBuilderAppendable
extends|extends
name|StringBuilderAppendable
block|{
annotation|@
name|Override
specifier|public
name|QuotingAppendable
name|appendQuoted
parameter_list|(
name|CharSequence
name|csq
parameter_list|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|'`'
argument_list|)
operator|.
name|append
argument_list|(
name|csq
argument_list|)
operator|.
name|append
argument_list|(
literal|'`'
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
block|}
end_class

end_unit

