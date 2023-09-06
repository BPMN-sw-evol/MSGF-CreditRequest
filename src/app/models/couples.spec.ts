import { Couples } from './couples';

describe('Couples', () => {
  let couples: Couples;

  beforeEach(() => {
    couples = new Couples();
  });

  it('should have an id of 0', () => {
    expect(couples.id).toEqual(0);
  });

  it('should have a name of an empty string', () => {
    expect(couples.name).toEqual('');
  });

  it('should have a lastname of an empty string', () => {
    expect(couples.lastname).toEqual('');
  });
});