class BackupThread extends Thread {
	public Map<String, Restaurant> processFile(String aURLString, Date aCutoffDate) throws MalformedURLException, IOException {
		LOGGER.info(format("Starting to process URL %s", aURLString));

		final Map<String, Restaurant> result = new HashMap<>();
		final long startTime = System.currentTimeMillis();
		final URL url = new URL(aURLString);
		final URLConnection connection = url.openConnection();
		final InputStream inputStream = connection.getInputStream();
		try {
			final ZipInputStream zipInputStream = new ZipInputStream(inputStream);
			ZipEntry zipEntry;
			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
				if (zipEntry.getName().equals("WebExtract.txt")) {
					final BufferedReader br = new BufferedReader(new InputStreamReader(zipInputStream));

					// skip over the header
					final String header = br.readLine();
					if (!header.equals(EXPECTED_HEADER_FOR_WEB_EXTRACT)) {
						throw new RuntimeException(format("Received unexpected header for WebExtract.txt: %s", header));
					}

					String line;
					while ((line = br.readLine()) != null) {
						try {
							final Matcher matcher = PATTERN.matcher(line);
							if (!matcher.matches()) {
								throw new RuntimeException(format("Couldn't match WebExtract.txt record to pattern: %s", line));
							}

							final String camis = matcher.group(1);
							final Date gradeDate;
							gradeDate = convertStringToDate(matcher.group(14));

							// if this restaurant is not in the result yet, or else is, but this has a less recent
							// grade, then store it
							// TODO probably should be looking at the inspection date, not the grade date
							if (!result.containsKey(camis) || !isLaterThan(gradeDate, result.get(camis).getGradeDate())) {
								final String doingBusinessAs = matcher.group(2).trim();
								final Borough borough = Borough.findByCode(Integer.parseInt(matcher.group(3)));
								final String building = matcher.group(4).trim();
								final String street = matcher.group(5).trim();
								final String zipCode = matcher.group(6);
								final String phoneNumber = matcher.group(7);
								final String cuisine = matcher.group(8);
								final Date inspectionDate = convertStringToDate(matcher.group(9));
								final Grade currentGrade = Grade.findByCode(matcher.group(13));
								final Restaurant restaurant =
										new Restaurant(camis, doingBusinessAs, borough, new Address(building, street, zipCode), phoneNumber, cuisine, currentGrade, gradeDate, inspectionDate);
								result.put(camis, restaurant);
							}
						} catch (Exception e) {
							LOGGER.log(WARNING, format("Unable to process record %s", line), e);
						}
					}
				}
			}
		} finally {
			inputStream.close();
		}

		final long endTime = System.currentTimeMillis();
		LOGGER.info(format("Finished processing URL %s in %,d ms", aURLString, (endTime - startTime)));

		return result;
	}
}
