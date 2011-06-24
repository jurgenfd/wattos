nose:
	# Write the .coverage and nosetest.xml
	nosetests --with-xunit --with-coverage --verbose --cover-package=Wattos --where=python/Wattos
	# Convert .coverage to coverage.xml
	coverage xml

pylint:
	cd python; pylint --rcfile .pylintrc Wattos > pylint.txt || exit 0
	
sloccount:
	sloccount --duplicates --wide --details src python > sloccount.sc