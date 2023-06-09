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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_comment
comment|/**  * Assists in implementing {@link java.lang.Comparable#compareTo(Object)} methods.  *  * This code is based on CompareToBuilder from commons-lang v2.6  *  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|CompareToBuilder
block|{
comment|/**      * Current state of the comparison as appended fields are checked.      */
specifier|private
name|int
name|comparison
decl_stmt|;
comment|/**      *<p>Constructor for CompareToBuilder.</p>      *      *<p>Starts off assuming that the objects are equal. Multiple calls are      * then made to the various append methods, followed by a call to      * {@link #toComparison} to get the result.</p>      */
specifier|public
name|CompareToBuilder
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|comparison
operator|=
literal|0
expr_stmt|;
block|}
comment|//-----------------------------------------------------------------------
comment|/**      *<p>Appends to the<code>builder</code> the<code>compareTo(Object)</code>      * result of the superclass.</p>      *      * @param superCompareTo  result of calling<code>super.compareTo(Object)</code>      * @return this - used to chain append calls      */
specifier|public
name|CompareToBuilder
name|appendSuper
parameter_list|(
name|int
name|superCompareTo
parameter_list|)
block|{
if|if
condition|(
name|comparison
operator|!=
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
name|comparison
operator|=
name|superCompareTo
expr_stmt|;
return|return
name|this
return|;
block|}
comment|//-----------------------------------------------------------------------
comment|/**      *<p>Appends to the<code>builder</code> the comparison of      * two<code>Object</code>s.</p>      *      *<ol>      *<li>Check if<code>lhs == rhs</code></li>      *<li>Check if either<code>lhs</code> or<code>rhs</code> is<code>null</code>,      *     a<code>null</code> object is less than a non-<code>null</code> object</li>      *<li>Check the object contents</li>      *</ol>      *      *<p><code>lhs</code> must either be an array or implement {@link Comparable}.</p>      *      * @param lhs  left-hand object      * @param rhs  right-hand object      * @return this - used to chain append calls      * @throws ClassCastException  if<code>rhs</code> is not assignment-compatible      *  with<code>lhs</code>      */
specifier|public
name|CompareToBuilder
name|append
parameter_list|(
name|Object
name|lhs
parameter_list|,
name|Object
name|rhs
parameter_list|)
block|{
return|return
name|append
argument_list|(
name|lhs
argument_list|,
name|rhs
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      *<p>Appends to the<code>builder</code> the comparison of      * two<code>Object</code>s.</p>      *      *<ol>      *<li>Check if<code>lhs == rhs</code></li>      *<li>Check if either<code>lhs</code> or<code>rhs</code> is<code>null</code>,      *     a<code>null</code> object is less than a non-<code>null</code> object</li>      *<li>Check the object contents</li>      *</ol>      *      *<p>If<code>lhs</code> is an array, array comparison methods will be used.      * Otherwise<code>comparator</code> will be used to compare the objects.      * If<code>comparator</code> is<code>null</code>,<code>lhs</code> must      * implement {@link Comparable} instead.</p>      *      * @param lhs  left-hand object      * @param rhs  right-hand object      * @param comparator<code>Comparator</code> used to compare the objects,      *<code>null</code> means treat lhs as<code>Comparable</code>      * @return this - used to chain append calls      * @throws ClassCastException  if<code>rhs</code> is not assignment-compatible      *  with<code>lhs</code>      * @since 2.0      */
specifier|public
name|CompareToBuilder
name|append
parameter_list|(
name|Object
name|lhs
parameter_list|,
name|Object
name|rhs
parameter_list|,
name|Comparator
name|comparator
parameter_list|)
block|{
if|if
condition|(
name|comparison
operator|!=
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|==
name|rhs
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|==
literal|null
condition|)
block|{
name|comparison
operator|=
operator|-
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
if|if
condition|(
name|rhs
operator|==
literal|null
condition|)
block|{
name|comparison
operator|=
operator|+
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
block|{
comment|// switch on type of array, to dispatch to the correct handler
comment|// handles multi dimensional arrays
comment|// throws a ClassCastException if rhs is not the correct array type
if|if
condition|(
name|lhs
operator|instanceof
name|long
index|[]
condition|)
block|{
name|append
argument_list|(
operator|(
name|long
index|[]
operator|)
name|lhs
argument_list|,
operator|(
name|long
index|[]
operator|)
name|rhs
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|lhs
operator|instanceof
name|int
index|[]
condition|)
block|{
name|append
argument_list|(
operator|(
name|int
index|[]
operator|)
name|lhs
argument_list|,
operator|(
name|int
index|[]
operator|)
name|rhs
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|lhs
operator|instanceof
name|short
index|[]
condition|)
block|{
name|append
argument_list|(
operator|(
name|short
index|[]
operator|)
name|lhs
argument_list|,
operator|(
name|short
index|[]
operator|)
name|rhs
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|lhs
operator|instanceof
name|char
index|[]
condition|)
block|{
name|append
argument_list|(
operator|(
name|char
index|[]
operator|)
name|lhs
argument_list|,
operator|(
name|char
index|[]
operator|)
name|rhs
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|lhs
operator|instanceof
name|byte
index|[]
condition|)
block|{
name|append
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|lhs
argument_list|,
operator|(
name|byte
index|[]
operator|)
name|rhs
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|lhs
operator|instanceof
name|double
index|[]
condition|)
block|{
name|append
argument_list|(
operator|(
name|double
index|[]
operator|)
name|lhs
argument_list|,
operator|(
name|double
index|[]
operator|)
name|rhs
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|lhs
operator|instanceof
name|float
index|[]
condition|)
block|{
name|append
argument_list|(
operator|(
name|float
index|[]
operator|)
name|lhs
argument_list|,
operator|(
name|float
index|[]
operator|)
name|rhs
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|lhs
operator|instanceof
name|boolean
index|[]
condition|)
block|{
name|append
argument_list|(
operator|(
name|boolean
index|[]
operator|)
name|lhs
argument_list|,
operator|(
name|boolean
index|[]
operator|)
name|rhs
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// not an array of primitives
comment|// throws a ClassCastException if rhs is not an array
name|append
argument_list|(
operator|(
name|Object
index|[]
operator|)
name|lhs
argument_list|,
operator|(
name|Object
index|[]
operator|)
name|rhs
argument_list|,
name|comparator
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// the simple case, not an array, just test the element
if|if
condition|(
name|comparator
operator|==
literal|null
condition|)
block|{
name|comparison
operator|=
operator|(
operator|(
name|Comparable
operator|)
name|lhs
operator|)
operator|.
name|compareTo
argument_list|(
name|rhs
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|comparison
operator|=
name|comparator
operator|.
name|compare
argument_list|(
name|lhs
argument_list|,
name|rhs
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|this
return|;
block|}
comment|//-------------------------------------------------------------------------
comment|/**      * Appends to the<code>builder</code> the comparison of      * two<code>long</code>s.      *      * @param lhs  left-hand value      * @param rhs  right-hand value      * @return this - used to chain append calls      */
specifier|public
name|CompareToBuilder
name|append
parameter_list|(
name|long
name|lhs
parameter_list|,
name|long
name|rhs
parameter_list|)
block|{
if|if
condition|(
name|comparison
operator|!=
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
name|comparison
operator|=
name|Long
operator|.
name|compare
argument_list|(
name|lhs
argument_list|,
name|rhs
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Appends to the<code>builder</code> the comparison of      * two<code>int</code>s.      *      * @param lhs  left-hand value      * @param rhs  right-hand value      * @return this - used to chain append calls      */
specifier|public
name|CompareToBuilder
name|append
parameter_list|(
name|int
name|lhs
parameter_list|,
name|int
name|rhs
parameter_list|)
block|{
if|if
condition|(
name|comparison
operator|!=
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
name|comparison
operator|=
name|Integer
operator|.
name|compare
argument_list|(
name|lhs
argument_list|,
name|rhs
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Appends to the<code>builder</code> the comparison of      * two<code>short</code>s.      *      * @param lhs  left-hand value      * @param rhs  right-hand value      * @return this - used to chain append calls      */
specifier|public
name|CompareToBuilder
name|append
parameter_list|(
name|short
name|lhs
parameter_list|,
name|short
name|rhs
parameter_list|)
block|{
if|if
condition|(
name|comparison
operator|!=
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
name|comparison
operator|=
name|Short
operator|.
name|compare
argument_list|(
name|lhs
argument_list|,
name|rhs
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Appends to the<code>builder</code> the comparison of      * two<code>char</code>s.      *      * @param lhs  left-hand value      * @param rhs  right-hand value      * @return this - used to chain append calls      */
specifier|public
name|CompareToBuilder
name|append
parameter_list|(
name|char
name|lhs
parameter_list|,
name|char
name|rhs
parameter_list|)
block|{
if|if
condition|(
name|comparison
operator|!=
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
name|comparison
operator|=
name|Character
operator|.
name|compare
argument_list|(
name|lhs
argument_list|,
name|rhs
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Appends to the<code>builder</code> the comparison of      * two<code>byte</code>s.      *      * @param lhs  left-hand value      * @param rhs  right-hand value      * @return this - used to chain append calls      */
specifier|public
name|CompareToBuilder
name|append
parameter_list|(
name|byte
name|lhs
parameter_list|,
name|byte
name|rhs
parameter_list|)
block|{
if|if
condition|(
name|comparison
operator|!=
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
name|comparison
operator|=
name|Byte
operator|.
name|compare
argument_list|(
name|lhs
argument_list|,
name|rhs
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      *<p>Appends to the<code>builder</code> the comparison of      * two<code>double</code>s.</p>      *      *<p>This handles NaNs, Infinities, and<code>-0.0</code>.</p>      *      *<p>It is compatible with the hash code generated by      *<code>HashCodeBuilder</code>.</p>      *      * @param lhs  left-hand value      * @param rhs  right-hand value      * @return this - used to chain append calls      */
specifier|public
name|CompareToBuilder
name|append
parameter_list|(
name|double
name|lhs
parameter_list|,
name|double
name|rhs
parameter_list|)
block|{
if|if
condition|(
name|comparison
operator|!=
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
name|comparison
operator|=
name|Double
operator|.
name|compare
argument_list|(
name|lhs
argument_list|,
name|rhs
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      *<p>Appends to the<code>builder</code> the comparison of      * two<code>float</code>s.</p>      *      *<p>This handles NaNs, Infinities, and<code>-0.0</code>.</p>      *      *<p>It is compatible with the hash code generated by      *<code>HashCodeBuilder</code>.</p>      *      * @param lhs  left-hand value      * @param rhs  right-hand value      * @return this - used to chain append calls      */
specifier|public
name|CompareToBuilder
name|append
parameter_list|(
name|float
name|lhs
parameter_list|,
name|float
name|rhs
parameter_list|)
block|{
if|if
condition|(
name|comparison
operator|!=
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
name|comparison
operator|=
name|Float
operator|.
name|compare
argument_list|(
name|lhs
argument_list|,
name|rhs
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Appends to the<code>builder</code> the comparison of      * two<code>booleans</code>s.      *      * @param lhs  left-hand value      * @param rhs  right-hand value      * @return this - used to chain append calls      */
specifier|public
name|CompareToBuilder
name|append
parameter_list|(
name|boolean
name|lhs
parameter_list|,
name|boolean
name|rhs
parameter_list|)
block|{
if|if
condition|(
name|comparison
operator|!=
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|==
name|rhs
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
operator|!
name|lhs
condition|)
block|{
name|comparison
operator|=
operator|-
literal|1
expr_stmt|;
block|}
else|else
block|{
name|comparison
operator|=
operator|+
literal|1
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|//-----------------------------------------------------------------------
comment|/**      *<p>Appends to the<code>builder</code> the deep comparison of      * two<code>Object</code> arrays.</p>      *      *<ol>      *<li>Check if arrays are the same using<code>==</code></li>      *<li>Check if for<code>null</code>,<code>null</code> is less than non-<code>null</code></li>      *<li>Check array length, a short length array is less than a long length array</li>      *<li>Check array contents element by element using {@link #append(Object, Object, Comparator)}</li>      *</ol>      *      *<p>This method will also will be called for the top level of multi-dimensional,      * ragged, and multi-typed arrays.</p>      *      * @param lhs  left-hand array      * @param rhs  right-hand array      * @return this - used to chain append calls      * @throws ClassCastException  if<code>rhs</code> is not assignment-compatible      *  with<code>lhs</code>      */
specifier|public
name|CompareToBuilder
name|append
parameter_list|(
name|Object
index|[]
name|lhs
parameter_list|,
name|Object
index|[]
name|rhs
parameter_list|)
block|{
return|return
name|append
argument_list|(
name|lhs
argument_list|,
name|rhs
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      *<p>Appends to the<code>builder</code> the deep comparison of      * two<code>Object</code> arrays.</p>      *      *<ol>      *<li>Check if arrays are the same using<code>==</code></li>      *<li>Check if for<code>null</code>,<code>null</code> is less than non-<code>null</code></li>      *<li>Check array length, a short length array is less than a long length array</li>      *<li>Check array contents element by element using {@link #append(Object, Object, Comparator)}</li>      *</ol>      *      *<p>This method will also will be called for the top level of multi-dimensional,      * ragged, and multi-typed arrays.</p>      *      * @param lhs  left-hand array      * @param rhs  right-hand array      * @param comparator<code>Comparator</code> to use to compare the array elements,      *<code>null</code> means to treat<code>lhs</code> elements as<code>Comparable</code>.      * @return this - used to chain append calls      * @throws ClassCastException  if<code>rhs</code> is not assignment-compatible      *  with<code>lhs</code>      * @since 2.0      */
specifier|public
name|CompareToBuilder
name|append
parameter_list|(
name|Object
index|[]
name|lhs
parameter_list|,
name|Object
index|[]
name|rhs
parameter_list|,
name|Comparator
name|comparator
parameter_list|)
block|{
if|if
condition|(
name|comparison
operator|!=
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|==
name|rhs
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|==
literal|null
condition|)
block|{
name|comparison
operator|=
operator|-
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
if|if
condition|(
name|rhs
operator|==
literal|null
condition|)
block|{
name|comparison
operator|=
operator|+
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|.
name|length
operator|!=
name|rhs
operator|.
name|length
condition|)
block|{
name|comparison
operator|=
operator|(
name|lhs
operator|.
name|length
operator|<
name|rhs
operator|.
name|length
operator|)
condition|?
operator|-
literal|1
else|:
operator|+
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|lhs
operator|.
name|length
operator|&&
name|comparison
operator|==
literal|0
condition|;
name|i
operator|++
control|)
block|{
name|append
argument_list|(
name|lhs
index|[
name|i
index|]
argument_list|,
name|rhs
index|[
name|i
index|]
argument_list|,
name|comparator
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      *<p>Appends to the<code>builder</code> the deep comparison of      * two<code>long</code> arrays.</p>      *      *<ol>      *<li>Check if arrays are the same using<code>==</code></li>      *<li>Check if for<code>null</code>,<code>null</code> is less than non-<code>null</code></li>      *<li>Check array length, a shorter length array is less than a longer length array</li>      *<li>Check array contents element by element using {@link #append(long, long)}</li>      *</ol>      *      * @param lhs  left-hand array      * @param rhs  right-hand array      * @return this - used to chain append calls      */
specifier|public
name|CompareToBuilder
name|append
parameter_list|(
name|long
index|[]
name|lhs
parameter_list|,
name|long
index|[]
name|rhs
parameter_list|)
block|{
if|if
condition|(
name|comparison
operator|!=
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|==
name|rhs
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|==
literal|null
condition|)
block|{
name|comparison
operator|=
operator|-
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
if|if
condition|(
name|rhs
operator|==
literal|null
condition|)
block|{
name|comparison
operator|=
operator|+
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|.
name|length
operator|!=
name|rhs
operator|.
name|length
condition|)
block|{
name|comparison
operator|=
operator|(
name|lhs
operator|.
name|length
operator|<
name|rhs
operator|.
name|length
operator|)
condition|?
operator|-
literal|1
else|:
operator|+
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|lhs
operator|.
name|length
operator|&&
name|comparison
operator|==
literal|0
condition|;
name|i
operator|++
control|)
block|{
name|append
argument_list|(
name|lhs
index|[
name|i
index|]
argument_list|,
name|rhs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      *<p>Appends to the<code>builder</code> the deep comparison of      * two<code>int</code> arrays.</p>      *      *<ol>      *<li>Check if arrays are the same using<code>==</code></li>      *<li>Check if for<code>null</code>,<code>null</code> is less than non-<code>null</code></li>      *<li>Check array length, a shorter length array is less than a longer length array</li>      *<li>Check array contents element by element using {@link #append(int, int)}</li>      *</ol>      *      * @param lhs  left-hand array      * @param rhs  right-hand array      * @return this - used to chain append calls      */
specifier|public
name|CompareToBuilder
name|append
parameter_list|(
name|int
index|[]
name|lhs
parameter_list|,
name|int
index|[]
name|rhs
parameter_list|)
block|{
if|if
condition|(
name|comparison
operator|!=
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|==
name|rhs
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|==
literal|null
condition|)
block|{
name|comparison
operator|=
operator|-
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
if|if
condition|(
name|rhs
operator|==
literal|null
condition|)
block|{
name|comparison
operator|=
operator|+
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|.
name|length
operator|!=
name|rhs
operator|.
name|length
condition|)
block|{
name|comparison
operator|=
operator|(
name|lhs
operator|.
name|length
operator|<
name|rhs
operator|.
name|length
operator|)
condition|?
operator|-
literal|1
else|:
operator|+
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|lhs
operator|.
name|length
operator|&&
name|comparison
operator|==
literal|0
condition|;
name|i
operator|++
control|)
block|{
name|append
argument_list|(
name|lhs
index|[
name|i
index|]
argument_list|,
name|rhs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      *<p>Appends to the<code>builder</code> the deep comparison of      * two<code>short</code> arrays.</p>      *      *<ol>      *<li>Check if arrays are the same using<code>==</code></li>      *<li>Check if for<code>null</code>,<code>null</code> is less than non-<code>null</code></li>      *<li>Check array length, a shorter length array is less than a longer length array</li>      *<li>Check array contents element by element using {@link #append(short, short)}</li>      *</ol>      *      * @param lhs  left-hand array      * @param rhs  right-hand array      * @return this - used to chain append calls      */
specifier|public
name|CompareToBuilder
name|append
parameter_list|(
name|short
index|[]
name|lhs
parameter_list|,
name|short
index|[]
name|rhs
parameter_list|)
block|{
if|if
condition|(
name|comparison
operator|!=
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|==
name|rhs
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|==
literal|null
condition|)
block|{
name|comparison
operator|=
operator|-
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
if|if
condition|(
name|rhs
operator|==
literal|null
condition|)
block|{
name|comparison
operator|=
operator|+
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|.
name|length
operator|!=
name|rhs
operator|.
name|length
condition|)
block|{
name|comparison
operator|=
operator|(
name|lhs
operator|.
name|length
operator|<
name|rhs
operator|.
name|length
operator|)
condition|?
operator|-
literal|1
else|:
operator|+
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|lhs
operator|.
name|length
operator|&&
name|comparison
operator|==
literal|0
condition|;
name|i
operator|++
control|)
block|{
name|append
argument_list|(
name|lhs
index|[
name|i
index|]
argument_list|,
name|rhs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      *<p>Appends to the<code>builder</code> the deep comparison of      * two<code>char</code> arrays.</p>      *      *<ol>      *<li>Check if arrays are the same using<code>==</code></li>      *<li>Check if for<code>null</code>,<code>null</code> is less than non-<code>null</code></li>      *<li>Check array length, a shorter length array is less than a longer length array</li>      *<li>Check array contents element by element using {@link #append(char, char)}</li>      *</ol>      *      * @param lhs  left-hand array      * @param rhs  right-hand array      * @return this - used to chain append calls      */
specifier|public
name|CompareToBuilder
name|append
parameter_list|(
name|char
index|[]
name|lhs
parameter_list|,
name|char
index|[]
name|rhs
parameter_list|)
block|{
if|if
condition|(
name|comparison
operator|!=
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|==
name|rhs
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|==
literal|null
condition|)
block|{
name|comparison
operator|=
operator|-
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
if|if
condition|(
name|rhs
operator|==
literal|null
condition|)
block|{
name|comparison
operator|=
operator|+
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|.
name|length
operator|!=
name|rhs
operator|.
name|length
condition|)
block|{
name|comparison
operator|=
operator|(
name|lhs
operator|.
name|length
operator|<
name|rhs
operator|.
name|length
operator|)
condition|?
operator|-
literal|1
else|:
operator|+
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|lhs
operator|.
name|length
operator|&&
name|comparison
operator|==
literal|0
condition|;
name|i
operator|++
control|)
block|{
name|append
argument_list|(
name|lhs
index|[
name|i
index|]
argument_list|,
name|rhs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      *<p>Appends to the<code>builder</code> the deep comparison of      * two<code>byte</code> arrays.</p>      *      *<ol>      *<li>Check if arrays are the same using<code>==</code></li>      *<li>Check if for<code>null</code>,<code>null</code> is less than non-<code>null</code></li>      *<li>Check array length, a shorter length array is less than a longer length array</li>      *<li>Check array contents element by element using {@link #append(byte, byte)}</li>      *</ol>      *      * @param lhs  left-hand array      * @param rhs  right-hand array      * @return this - used to chain append calls      */
specifier|public
name|CompareToBuilder
name|append
parameter_list|(
name|byte
index|[]
name|lhs
parameter_list|,
name|byte
index|[]
name|rhs
parameter_list|)
block|{
if|if
condition|(
name|comparison
operator|!=
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|==
name|rhs
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|==
literal|null
condition|)
block|{
name|comparison
operator|=
operator|-
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
if|if
condition|(
name|rhs
operator|==
literal|null
condition|)
block|{
name|comparison
operator|=
operator|+
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|.
name|length
operator|!=
name|rhs
operator|.
name|length
condition|)
block|{
name|comparison
operator|=
operator|(
name|lhs
operator|.
name|length
operator|<
name|rhs
operator|.
name|length
operator|)
condition|?
operator|-
literal|1
else|:
operator|+
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|lhs
operator|.
name|length
operator|&&
name|comparison
operator|==
literal|0
condition|;
name|i
operator|++
control|)
block|{
name|append
argument_list|(
name|lhs
index|[
name|i
index|]
argument_list|,
name|rhs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      *<p>Appends to the<code>builder</code> the deep comparison of      * two<code>double</code> arrays.</p>      *      *<ol>      *<li>Check if arrays are the same using<code>==</code></li>      *<li>Check if for<code>null</code>,<code>null</code> is less than non-<code>null</code></li>      *<li>Check array length, a shorter length array is less than a longer length array</li>      *<li>Check array contents element by element using {@link #append(double, double)}</li>      *</ol>      *      * @param lhs  left-hand array      * @param rhs  right-hand array      * @return this - used to chain append calls      */
specifier|public
name|CompareToBuilder
name|append
parameter_list|(
name|double
index|[]
name|lhs
parameter_list|,
name|double
index|[]
name|rhs
parameter_list|)
block|{
if|if
condition|(
name|comparison
operator|!=
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|==
name|rhs
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|==
literal|null
condition|)
block|{
name|comparison
operator|=
operator|-
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
if|if
condition|(
name|rhs
operator|==
literal|null
condition|)
block|{
name|comparison
operator|=
operator|+
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|.
name|length
operator|!=
name|rhs
operator|.
name|length
condition|)
block|{
name|comparison
operator|=
operator|(
name|lhs
operator|.
name|length
operator|<
name|rhs
operator|.
name|length
operator|)
condition|?
operator|-
literal|1
else|:
operator|+
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|lhs
operator|.
name|length
operator|&&
name|comparison
operator|==
literal|0
condition|;
name|i
operator|++
control|)
block|{
name|append
argument_list|(
name|lhs
index|[
name|i
index|]
argument_list|,
name|rhs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      *<p>Appends to the<code>builder</code> the deep comparison of      * two<code>float</code> arrays.</p>      *      *<ol>      *<li>Check if arrays are the same using<code>==</code></li>      *<li>Check if for<code>null</code>,<code>null</code> is less than non-<code>null</code></li>      *<li>Check array length, a shorter length array is less than a longer length array</li>      *<li>Check array contents element by element using {@link #append(float, float)}</li>      *</ol>      *      * @param lhs  left-hand array      * @param rhs  right-hand array      * @return this - used to chain append calls      */
specifier|public
name|CompareToBuilder
name|append
parameter_list|(
name|float
index|[]
name|lhs
parameter_list|,
name|float
index|[]
name|rhs
parameter_list|)
block|{
if|if
condition|(
name|comparison
operator|!=
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|==
name|rhs
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|==
literal|null
condition|)
block|{
name|comparison
operator|=
operator|-
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
if|if
condition|(
name|rhs
operator|==
literal|null
condition|)
block|{
name|comparison
operator|=
operator|+
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|.
name|length
operator|!=
name|rhs
operator|.
name|length
condition|)
block|{
name|comparison
operator|=
operator|(
name|lhs
operator|.
name|length
operator|<
name|rhs
operator|.
name|length
operator|)
condition|?
operator|-
literal|1
else|:
operator|+
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|lhs
operator|.
name|length
operator|&&
name|comparison
operator|==
literal|0
condition|;
name|i
operator|++
control|)
block|{
name|append
argument_list|(
name|lhs
index|[
name|i
index|]
argument_list|,
name|rhs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      *<p>Appends to the<code>builder</code> the deep comparison of      * two<code>boolean</code> arrays.</p>      *      *<ol>      *<li>Check if arrays are the same using<code>==</code></li>      *<li>Check if for<code>null</code>,<code>null</code> is less than non-<code>null</code></li>      *<li>Check array length, a shorter length array is less than a longer length array</li>      *<li>Check array contents element by element using {@link #append(boolean, boolean)}</li>      *</ol>      *      * @param lhs  left-hand array      * @param rhs  right-hand array      * @return this - used to chain append calls      */
specifier|public
name|CompareToBuilder
name|append
parameter_list|(
name|boolean
index|[]
name|lhs
parameter_list|,
name|boolean
index|[]
name|rhs
parameter_list|)
block|{
if|if
condition|(
name|comparison
operator|!=
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|==
name|rhs
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|==
literal|null
condition|)
block|{
name|comparison
operator|=
operator|-
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
if|if
condition|(
name|rhs
operator|==
literal|null
condition|)
block|{
name|comparison
operator|=
operator|+
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
if|if
condition|(
name|lhs
operator|.
name|length
operator|!=
name|rhs
operator|.
name|length
condition|)
block|{
name|comparison
operator|=
operator|(
name|lhs
operator|.
name|length
operator|<
name|rhs
operator|.
name|length
operator|)
condition|?
operator|-
literal|1
else|:
operator|+
literal|1
expr_stmt|;
return|return
name|this
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|lhs
operator|.
name|length
operator|&&
name|comparison
operator|==
literal|0
condition|;
name|i
operator|++
control|)
block|{
name|append
argument_list|(
name|lhs
index|[
name|i
index|]
argument_list|,
name|rhs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|//-----------------------------------------------------------------------
comment|/**      * Returns a negative integer, a positive integer, or zero as      * the<code>builder</code> has judged the "left-hand" side      * as less than, greater than, or equal to the "right-hand"      * side.      *      * @return final comparison result      */
specifier|public
name|int
name|toComparison
parameter_list|()
block|{
return|return
name|comparison
return|;
block|}
block|}
end_class

end_unit

