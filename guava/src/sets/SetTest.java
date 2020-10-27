package sets

/**
 * Test class for Guava Sets
 * @author Baeldung
 * @see https://www.baeldung.com/guava-sets?tl_inbound=1&tl_period_type=3&tl_target_all=1&utm_campaign=auto_44_guava&utm_medium=email&utm_source=email-newsletter
 */
class SetTest {
   @Test
	public void whenCalculatingUnionOfSets_thenCorrect() {
	    Set<Character> first = ImmutableSet.of('a', 'b', 'c');
	    Set<Character> second = ImmutableSet.of('b', 'c', 'd');
	 
	    Set<Character> union = Sets.union(first, second);
	    assertThat(union, containsInAnyOrder('a', 'b', 'c', 'd'));
   }
   
   @Test
	public void whenCalculatingCartesianProductOfSets_thenCorrect() {
	    Set<Character> first = ImmutableSet.of('a', 'b');
	    Set<Character> second = ImmutableSet.of('c', 'd');
	    Set<List<Character>> result =
	      Sets.cartesianProduct(ImmutableList.of(first, second));
	 
	    Function<List<Character>, String> func =
	      new Function<List<Character>, String>() {
	        public String apply(List<Character> input) {
	            return Joiner.on(" ").join(input);
	        }
	    };
	    Iterable<String> joined = Iterables.transform(result, func);
	    assertThat(joined, containsInAnyOrder("a c", "a d", "b c", "b d"));
	}

   @Test
	public void whenCalculatingSetIntersection_thenCorrect() {
	    Set<Character> first = ImmutableSet.of('a', 'b', 'c');
	    Set<Character> second = ImmutableSet.of('b', 'c', 'd');
	 
	    Set<Character> intersection = Sets.intersection(first, second);
	    assertThat(intersection, containsInAnyOrder('b', 'c'));
   }
   
   /**
    * Elementos que no están ni en A ni en B
    */
   @Test
	public void whenCalculatingSetSymmetricDifference_thenCorrect() {
	    Set<Character> A = ImmutableSet.of('a', 'b', 'c');
	    Set<Character> B = ImmutableSet.of('b', 'c', 'd');
	 
	    Set<Character> intersection = Sets.symmetricDifference(A, B);
	    assertThat(intersection, containsInAnyOrder('a', 'd'));
   }
   
   @Test
	public void whenCalculatingPowerSet_thenCorrect() {
	    Set<Character> chars = ImmutableSet.of('a', 'b');
	 
	    Set<Set<Character>> result = Sets.powerSet(chars);
	 
	    Set<Character> empty =  ImmutableSet.<Character> builder().build();
	    Set<Character> a = ImmutableSet.of('a');
	    Set<Character> b = ImmutableSet.of('b');
	    Set<Character> aB = ImmutableSet.of('a', 'b');
	 
	    assertThat(result, contains(empty, a, b, aB));
   }
   
   @Test
	public void whenCreatingRangeOfIntegersSet_thenCreated() {
	    int start = 10;
	    int end = 30;
	    ContiguousSet<Integer> set = ContiguousSet.create(
	      Range.closed(start, end), DiscreteDomain.integers());
	 
	    assertEquals(21, set.size());
	    assertEquals(10, set.first().intValue());
	    assertEquals(30, set.last().intValue());
	}

	@Test
	public void whenUsingRangeSet_thenCorrect() {
	    RangeSet<Integer> rangeSet = TreeRangeSet.create();
	    rangeSet.add(Range.closed(1, 10));
	    rangeSet.add(Range.closed(12, 15));
	 
	    assertEquals(2, rangeSet.asRanges().size());
	 
	    rangeSet.add(Range.closed(10, 12));
	    assertTrue(rangeSet.encloses(Range.closed(1, 15)));
	    assertEquals(1, rangeSet.asRanges().size());
	}

	@Test
	public void whenInsertDuplicatesInMultiSet_thenInserted() {
	    Multiset<String> names = HashMultiset.create();
	    names.add("John");
	    names.add("Adam", 3);
	    names.add("John");
	 
	    assertEquals(2, names.count("John"));
	    names.remove("John");
	    assertEquals(1, names.count("John"));
	 
	    assertEquals(3, names.count("Adam"));
	    names.remove("Adam", 2);
	    assertEquals(1, names.count("Adam"));
	}

	@Test
	public void whenGetTopOcurringElementsWithMultiSet_thenCorrect() {
	    Multiset<String> names = HashMultiset.create();
	    names.add("John");
	    names.add("Adam", 5);
	    names.add("Jane");
	    names.add("Tom", 2);
	 
	    Set<String> sorted = Multisets.copyHighestCountFirst(names).elementSet();
	    List<String> sortedAsList = Lists.newArrayList(sorted);
	    assertEquals("Adam", sortedAsList.get(0));
	    assertEquals("Tom", sortedAsList.get(1));
	}
}