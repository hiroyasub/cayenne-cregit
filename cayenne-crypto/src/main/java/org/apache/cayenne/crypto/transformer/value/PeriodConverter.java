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
name|crypto
operator|.
name|transformer
operator|.
name|value
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Period
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|PeriodConverter
implements|implements
name|BytesConverter
argument_list|<
name|Period
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_CHARSET
init|=
literal|"UTF-8"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|BytesConverter
argument_list|<
name|Period
argument_list|>
name|INSTANCE
init|=
operator|new
name|PeriodConverter
argument_list|()
decl_stmt|;
specifier|private
name|Charset
name|utf8
decl_stmt|;
name|PeriodConverter
parameter_list|()
block|{
name|this
operator|.
name|utf8
operator|=
name|Charset
operator|.
name|forName
argument_list|(
name|DEFAULT_CHARSET
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Period
name|fromBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
name|Period
operator|.
name|parse
argument_list|(
operator|new
name|String
argument_list|(
name|bytes
argument_list|,
name|utf8
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|toBytes
parameter_list|(
name|Period
name|value
parameter_list|)
block|{
return|return
name|value
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
name|utf8
argument_list|)
return|;
block|}
block|}
end_class

end_unit

