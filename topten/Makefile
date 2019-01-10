large.txt: src/main/resources/text/lorem.txt
	for i in {0..2500}; do cat $< >> $@; done
	ls -lh $@

small.txt: src/main/resources/text/lorem.txt
	head -c 1024 $< > $@
	ls -lh $@

clean:
	rm -f large.txt small.txt