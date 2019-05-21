begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_comment
comment|// This code used in docs too, so it should be formatted
end_comment

begin_comment
comment|// to 80 char line to fit in PDF.
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|tutorial
operator|.
name|persistent
package|;
end_package

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|LocalDate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|format
operator|.
name|DateTimeFormatter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|format
operator|.
name|DateTimeParseException
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
name|tutorial
operator|.
name|persistent
operator|.
name|auto
operator|.
name|_Artist
import|;
end_import

begin_comment
comment|// tag::content[]
end_comment

begin_class
specifier|public
class|class
name|Artist
extends|extends
name|_Artist
block|{
specifier|static
specifier|final
name|String
name|DEFAULT_DATE_FORMAT
init|=
literal|"yyyyMMdd"
decl_stmt|;
comment|/**      * Sets date of birth using a string in format yyyyMMdd.      */
specifier|public
name|void
name|setDateOfBirthString
parameter_list|(
name|String
name|yearMonthDay
parameter_list|)
block|{
if|if
condition|(
name|yearMonthDay
operator|==
literal|null
condition|)
block|{
name|setDateOfBirth
argument_list|(
literal|null
argument_list|)
expr_stmt|;
return|return;
block|}
name|LocalDate
name|date
decl_stmt|;
try|try
block|{
name|DateTimeFormatter
name|formatter
init|=
name|DateTimeFormatter
operator|.
name|ofPattern
argument_list|(
name|DEFAULT_DATE_FORMAT
argument_list|)
decl_stmt|;
name|date
operator|=
name|LocalDate
operator|.
name|parse
argument_list|(
name|yearMonthDay
argument_list|,
name|formatter
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DateTimeParseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"A date argument must be in format '"
operator|+
name|DEFAULT_DATE_FORMAT
operator|+
literal|"': "
operator|+
name|yearMonthDay
argument_list|)
throw|;
block|}
name|setDateOfBirth
argument_list|(
name|date
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|// end::content[]
end_comment

end_unit

